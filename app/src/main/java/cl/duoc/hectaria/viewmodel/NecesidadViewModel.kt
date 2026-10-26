package cl.duoc.hectaria.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import cl.duoc.hectaria.repository.NecesidadRepository

class NecesidadViewModel : ViewModel() {

    val cultivos = listOf("Maíz", "Trigo", "Arándanos", "Manzanos", "Uva de mesa")
    val servicios = listOf("Fumigación con dron", "Aplicación foliar", "Monitoreo de cultivo", "Siembra con dron")

    var cultivo by mutableStateOf("")
    var servicio by mutableStateOf("")
    var superficie by mutableStateOf("")
    var zona by mutableStateOf("")
    var fecha by mutableStateOf("")

    var errorCultivo by mutableStateOf("")
    var errorServicio by mutableStateOf("")
    var errorSuperficie by mutableStateOf("")
    var errorZona by mutableStateOf("")
    var errorFecha by mutableStateOf("")

    fun validarFormulario(): Boolean {
        errorCultivo = if (cultivo.isBlank()) "Debes elegir un cultivo" else ""
        errorServicio = if (servicio.isBlank()) "Debes elegir un servicio" else ""
        val hectareas = superficie.toIntOrNull()
        errorSuperficie = when {
            superficie.isBlank() -> "Debes ingresar la superficie"
            hectareas == null || hectareas <= 0 -> "La superficie debe ser mayor a 0"
            else -> ""
        }
        errorZona = if (zona.isBlank()) "Debes ingresar la zona del predio" else ""
        errorFecha = if (fecha.isBlank()) "Debes ingresar una fecha estimada" else ""
        return errorCultivo.isEmpty() && errorServicio.isEmpty() &&
                errorSuperficie.isEmpty() && errorZona.isEmpty() && errorFecha.isEmpty()
    }

    fun guardar() {
        NecesidadRepository.agregar(cultivo, servicio, superficie.toInt(), zona, fecha)
    }
}
