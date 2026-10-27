package cl.duoc.hectaria.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun buscar(correo: String, clave: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE sesionActiva = 1 LIMIT 1")
    suspend fun sesion(): Usuario?

    @Query("UPDATE usuarios SET sesionActiva = 1 WHERE correo = :correo")
    suspend fun abrirSesion(correo: String)

    @Query("UPDATE usuarios SET sesionActiva = 0")
    suspend fun cerrarSesiones()
}
