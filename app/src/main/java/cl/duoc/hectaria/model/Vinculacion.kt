package cl.duoc.hectaria.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vinculaciones")
data class Vinculacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prestador: String,
    val servicio: String,
    val cultivo: String
)
