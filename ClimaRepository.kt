package cl.duoc.hectaria.repository

import cl.duoc.hectaria.data.remote.ClimaInstance

object ClimaRepository {

    // decide si el clima sirve para fumigar o no
    fun evaluar(lluvia: Double?, viento: Double?): String {
        if (lluvia == null || viento == null) return "No hay datos del clima"
        return when {
            lluvia > 1 -> "Hoy no se recomienda fumigar: hay lluvia"
            viento > 25 -> "Hoy no se recomienda fumigar: viento muy fuerte"
            viento > 12 -> "Cuidado: hay viento fuerte para fumigar"
            else -> "Buen clima para fumigar hoy"
        }
    }

    suspend fun climaDeZona(zona: String): String {
        val nombre = zona.split(",").first().trim()
        val lugar = ClimaInstance.geo.buscarLugar(nombre).results
            ?.find { it.country_code == "CL" }
            ?: return "No encontramos la zona en el mapa del clima"
        val clima = ClimaInstance.clima.verClima(lugar.latitude, lugar.longitude).current
            ?: return "No hay datos del clima"
        val recomendacion = evaluar(clima.precipitation, clima.wind_speed_10m)
        val temperatura = clima.temperature_2m?.let { " (${it.toInt()}°C)" } ?: ""
        return "$recomendacion$temperatura"
    }
}
