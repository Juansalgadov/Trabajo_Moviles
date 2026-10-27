package cl.duoc.hectaria.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val correo: String,
    val nombre: String,
    val clave: String,
    val tipo: String,
    val sesionActiva: Boolean = false
)
