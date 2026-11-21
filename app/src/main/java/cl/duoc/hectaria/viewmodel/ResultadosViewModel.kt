package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.data.model.VinculacionApi
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.model.Prestador
import cl.duoc.hectaria.model.Vinculacion
import cl.duoc.hectaria.repository.ClimaRepository
import cl.duoc.hectaria.repository.PrestadorRepository
import kotlinx.coroutines.launch

class ResultadosViewModel(application: Application) : AndroidViewModel(application) {

    private val base = AppDatabase.obtener(application)

    var necesidad by mutableStateOf<Necesidad?>(null)
    var cargado by mutableStateOf(false)
    var clima by mutableStateOf("")

    fun cargar() {
        viewModelScope.launch {
            try {
                if (PrestadorRepository.prestadores.isEmpty()) PrestadorRepository.cargarDesdeApi()
            } catch (e: Exception) {
                // sin internet dejamos lo que haya
            }
            necesidad = base.necesidadDao().ultima()
            cargado = true
            clima = try {
                ClimaRepository.climaDeZona(necesidad?.zona ?: "")
            } catch (e: Exception) {
                ""
            }
        }
    }

    suspend fun obtenerPrestador(id: Int): Prestador? {
        return PrestadorRepository.buscarPorId(id)
    }

    fun vincular(prestador: Prestador, alVincular: () -> Unit) {
        val actual = necesidad ?: return
        viewModelScope.launch {
            val puntos = PrestadorRepository.calcularCompatibilidad(prestador, actual)
            val sesion = base.usuarioDao().sesion()
            try {
                RetrofitInstance.api.guardarVinculacion(
                    VinculacionApi(
                        correo_usuario = sesion?.correo ?: "",
                        prestador_id = prestador.id,
                        prestador_nombre = prestador.nombre,
                        compatibilidad = puntos,
                        servicio = actual.servicio,
                        cultivo = actual.cultivo
                    )
                )
            } catch (e: Exception) {
                // si falla la api igual dejamos la vinculacion local
            }
            base.vinculacionDao().insertar(
                Vinculacion(prestador = prestador.nombre, servicio = actual.servicio, cultivo = actual.cultivo)
            )
            alVincular()
        }
    }
}
