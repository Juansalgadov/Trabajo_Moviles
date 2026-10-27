package cl.duoc.hectaria.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PerfilViewModel : ViewModel() {

    val serviciosDisponibles = listOf("Fumigación con dron", "Aplicación foliar", "Monitoreo de cultivo", "Siembra con dron")

    var serviciosElegidos by mutableStateOf(listOf("Fumigación con dron"))
    var cobertura by mutableStateOf("")
    var capacidad by mutableStateOf("")
    var disponibleDesde by mutableStateOf("")
    var experiencia by mutableStateOf("")

    var errorCobertura by mutableStateOf("")
    var errorCapacidad by mutableStateOf("")

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
}
