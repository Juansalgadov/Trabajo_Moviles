package cl.duoc.hectaria.model

data class Necesidad(
    val id: Int,
    val cultivo: String,
    val servicio: String,
    val superficie: Int,
    val zona: String,
    val fecha: String,
    val estado: String
)
