package cl.duoc.hectaria.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VinculacionDao {

    @Query("SELECT * FROM vinculaciones")
    suspend fun obtenerTodas(): List<Vinculacion>

    @Insert
    suspend fun insertar(vinculacion: Vinculacion)
}
