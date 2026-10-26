package cl.duoc.hectaria.model

data class Prestador(
    val id: Int,
    val nombre: String,
    val zona: String,
    val servicios: List<String>,
    val cultivos: List<String>,
    val capacidad: Int,
    val experiencia: Int,
    val disponible: Boolean,
    val evaluacion: Double
)
