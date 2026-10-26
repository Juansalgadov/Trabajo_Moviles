package cl.duoc.hectaria.repository

import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.model.Prestador

object PrestadorRepository {

    val prestadores = listOf(
        Prestador(1, "AgroDron Maule", "Linares", listOf("Fumigación con dron", "Monitoreo de cultivo"), listOf("Maíz", "Trigo"), 150, 6, true, 4.8),
        Prestador(2, "Servicios Aéreos del Sur", "Talca", listOf("Fumigación con dron", "Aplicación foliar"), listOf("Maíz", "Arándanos"), 100, 4, true, 4.5),
        Prestador(3, "DroneCampo SpA", "Parral", listOf("Fumigación con dron", "Siembra con dron"), listOf("Trigo", "Uva de mesa"), 80, 3, false, 4.2),
        Prestador(4, "AgroTec Parral", "Parral", listOf("Monitoreo de cultivo", "Aplicación foliar"), listOf("Maíz", "Manzanos"), 60, 5, true, 4.0),
        Prestador(5, "Fumigadora Ñuble", "Chillán", listOf("Fumigación con dron"), listOf("Arándanos", "Uva de mesa"), 40, 2, true, 3.9)
    )

    fun calcularCompatibilidad(prestador: Prestador, necesidad: Necesidad): Int {
        var puntos = 0
        if (necesidad.zona.contains(prestador.zona, ignoreCase = true)) puntos += 30
        if (necesidad.servicio in prestador.servicios) puntos += 30
        if (necesidad.cultivo in prestador.cultivos) puntos += 20
        if (prestador.capacidad >= necesidad.superficie) puntos += 10
        if (prestador.disponible) puntos += 10
        return puntos
    }

    fun buscarCompatibles(necesidad: Necesidad): List<Pair<Prestador, Int>> {
        return prestadores
            .map { it to calcularCompatibilidad(it, necesidad) }
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
    }

    fun buscarPorId(id: Int): Prestador? = prestadores.find { it.id == id }
}
