package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Usuario
import cl.duoc.hectaria.model.Vinculacion
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val base = AppDatabase.obtener(application)

    val serviciosDisponibles = listOf("Fumigación con dron", "Aplicación foliar", "Monitoreo de cultivo", "Siembra con dron")

    var usuario by mutableStateOf<Usuario?>(null)
    var vinculaciones by mutableStateOf(listOf<Vinculacion>())

    var serviciosElegidos by mutableStateOf(listOf("Fumigación con dron"))
    var cobertura by mutableStateOf("")
    var capacidad by mutableStateOf("")
    var disponibleDesde by mutableStateOf("")
    var experiencia by mutableStateOf("")

    var errorCobertura by mutableStateOf("")
    var errorCapacidad by mutableStateOf("")

    fun cargar() {
        viewModelScope.launch {
            usuario = base.usuarioDao().sesion()
            vinculaciones = base.vinculacionDao().obtenerTodas()
        }
    }

    fun alternarServicio(servicio: String) {
        serviciosElegidos = if (servicio in serviciosElegidos) {
            serviciosElegidos - servicio
        } else {
            serviciosElegidos + servicio
        }
    }

    fun validarFormulario(): Boolean {
        errorCobertura = if (cobertura.isBlank()) "Debes ingresar tu cobertura" else ""
        val hectareas = capacidad.toIntOrNull()
        errorCapacidad = when {
            capacidad.isBlank() -> "Debes ingresar tu capacidad"
            hectareas == null || hectareas <= 0 -> "La capacidad debe ser mayor a 0"
            else -> ""
        }
        return errorCobertura.isEmpty() && errorCapacidad.isEmpty()
    }

    fun cerrarSesion(alSalir: () -> Unit) {
        viewModelScope.launch {
            base.usuarioDao().cerrarSesiones()
            alSalir()
        }
    }
}
