package cl.duoc.hectaria.data.model

import cl.duoc.hectaria.model.Prestador

data class PrestadorApi(
    val id: Int? = null,
    val correo: String? = null,
    val nombre: String,
    val zona: String,
    val servicios: String,
    val cultivos: String = "",
    val capacidad: Int,
    val experiencia: Int,
    val disponible: Boolean = true,
    val evaluacion: Double = 4.0
)

// los datos que envia el prestador cuando se publica en el catalogo (sin id: lo asigna la base)
data class PublicarPrestador(
    val correo: String,
    val nombre: String,
    val zona: String,
    val servicios: String,
    val cultivos: String = "",
    val capacidad: Int,
    val experiencia: Int,
    val disponible: Boolean = true,
    val evaluacion: Double = 4.0
)

fun PrestadorApi.aPrestador(): Prestador {
    return Prestador(
        id = id ?: 0,
        nombre = nombre,
        zona = zona,
        servicios = servicios.split(",").map { it.trim() },
        cultivos = cultivos.split(",").map { it.trim() },
        capacidad = capacidad,
        experiencia = experiencia,
        disponible = disponible,
        evaluacion = evaluacion
    )
}

data class UsuarioApi(
    val correo: String,
    val nombre: String,
    val clave: String? = null,
    val tipo: String,
    val cobertura: String? = null,
    val capacidad: Int? = null,
    val experiencia: Int? = null
)

data class UsuarioPatch(
    val nombre: String? = null,
    val cobertura: String? = null,
    val capacidad: Int? = null,
    val experiencia: Int? = null
)

data class NecesidadApi(
    val id: Long? = null,
    val correo_usuario: String,
    val zona: String,
    val cultivo: String,
    val servicio: String,
    val superficie: Int,
    val fecha: String? = null
)

data class VinculacionApi(
    val id: Long? = null,
    val correo_usuario: String,
    val prestador_id: Int,
    val prestador_nombre: String,
    val compatibilidad: Int,
    val servicio: String,
    val cultivo: String,
    val fecha: String? = null
)

data class ClimaRespuesta(val current: ClimaActual? = null)

data class ClimaActual(
    val temperature_2m: Double? = null,
    val precipitation: Double? = null,
    val wind_speed_10m: Double? = null
)

data class LugaresRespuesta(val results: List<Lugar>? = null)

data class Lugar(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country_code: String? = null
)
