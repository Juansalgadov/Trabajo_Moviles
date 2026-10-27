package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.model.Vinculacion
import kotlinx.coroutines.launch

class ResultadosViewModel(application: Application) : AndroidViewModel(application) {

    private val base = AppDatabase.obtener(application)

    var necesidad by mutableStateOf<Necesidad?>(null)
    var cargado by mutableStateOf(false)

    fun cargar() {
        viewModelScope.launch {
            necesidad = base.necesidadDao().ultima()
            cargado = true
        }
    }

    fun vincular(prestador: String, alVincular: () -> Unit) {
        val actual = necesidad ?: return
        viewModelScope.launch {
            base.vinculacionDao().insertar(
                Vinculacion(prestador = prestador, servicio = actual.servicio, cultivo = actual.cultivo)
            )
            alVincular()
        }
    }
}
