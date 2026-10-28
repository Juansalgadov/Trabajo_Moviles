package cl.duoc.hectaria.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "necesidades")
data class Necesidad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cultivo: String,
    val servicio: String,
    val superficie: Int,
    val zona: String,
    val fecha: String,
    val estado: String
)
