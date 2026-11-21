package cl.duoc.hectaria.repository

import cl.duoc.hectaria.data.model.aPrestador
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.model.Prestador

object PrestadorRepository {

    var prestadores = listOf<Prestador>()

    suspend fun cargarDesdeApi() {
        prestadores = RetrofitInstance.api.obtenerPrestadores().map { it.aPrestador() }
    }

    suspend fun buscarPorId(id: Int): Prestador? {
        if (prestadores.isEmpty()) cargarDesdeApi()
        return prestadores.find { it.id == id }
    }

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
            .filter { (prestador, puntos) ->
                puntos > 0 && (necesidad.zona.contains(prestador.zona, ignoreCase = true) ||
                    necesidad.servicio in prestador.servicios ||
                    necesidad.cultivo in prestador.cultivos)
            }
            .sortedByDescending { it.second }
    }
}
