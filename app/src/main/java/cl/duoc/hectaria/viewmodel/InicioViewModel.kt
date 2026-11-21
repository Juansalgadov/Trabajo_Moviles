package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Necesidad
import kotlinx.coroutines.launch

class InicioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtener(application).necesidadDao()

    var necesidades by mutableStateOf(listOf<Necesidad>())

    fun cargar() {
        viewModelScope.launch {
            val sesion = AppDatabase.obtener(getApplication()).usuarioDao().sesion()
            necesidades = try {
                RetrofitInstance.api.obtenerNecesidades("eq." + (sesion?.correo ?: "")).map {
                    Necesidad(
                        cultivo = it.cultivo,
                        servicio = it.servicio,
                        superficie = it.superficie,
                        zona = it.zona,
                        fecha = it.fecha?.take(10)?.split("-")?.let { d -> d[2] + "/" + d[1] + "/" + d[0] } ?: "",
                        estado = "activa"
                    )
                }
            } catch (e: Exception) {
                dao.obtenerTodas()
            }
        }
    }
}
