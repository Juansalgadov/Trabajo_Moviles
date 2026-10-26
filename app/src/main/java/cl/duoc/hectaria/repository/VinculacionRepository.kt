package cl.duoc.hectaria.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cl.duoc.hectaria.model.Vinculacion

object VinculacionRepository {

    private var _vinculaciones by mutableStateOf(listOf<Vinculacion>())
    val vinculaciones: List<Vinculacion> get() = _vinculaciones

    private var siguienteId = 1

    fun agregar(prestador: String, servicio: String, cultivo: String) {
        _vinculaciones = _vinculaciones + Vinculacion(siguienteId++, prestador, servicio, cultivo)
    }
}
