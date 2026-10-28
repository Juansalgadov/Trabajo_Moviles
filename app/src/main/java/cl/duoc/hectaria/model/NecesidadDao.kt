package cl.duoc.hectaria.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NecesidadDao {

    @Query("SELECT * FROM necesidades")
    suspend fun obtenerTodas(): List<Necesidad>

    @Query("SELECT * FROM necesidades ORDER BY id DESC LIMIT 1")
    suspend fun ultima(): Necesidad?

    @Query("SELECT COUNT(*) FROM necesidades")
    suspend fun contar(): Int

    @Insert
    suspend fun insertar(necesidad: Necesidad)
}
