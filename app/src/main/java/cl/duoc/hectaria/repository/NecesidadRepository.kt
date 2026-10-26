package cl.duoc.hectaria.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cl.duoc.hectaria.model.Necesidad

object NecesidadRepository {

    private var _necesidades by mutableStateOf(
        listOf(
            Necesidad(1, "Maíz", "Fumigación con dron", 12, "Linares, Maule", "12/11/2026", "activa"),
            Necesidad(2, "Trigo", "Aplicación foliar", 30, "Parral, Maule", "20/11/2026", "en revisión"),
            Necesidad(3, "Arándanos", "Monitoreo de cultivo", 8, "San Javier, Maule", "05/10/2026", "cerrada")
        )
    )
    val necesidades: List<Necesidad> get() = _necesidades

    private var siguienteId = 4

    fun agregar(cultivo: String, servicio: String, superficie: Int, zona: String, fecha: String) {
        _necesidades = _necesidades + Necesidad(siguienteId++, cultivo, servicio, superficie, zona, fecha, "activa")
    }

    fun ultima(): Necesidad? = _necesidades.lastOrNull()
}
