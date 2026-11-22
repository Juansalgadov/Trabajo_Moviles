package cl.duoc.hectaria.data.remote

import cl.duoc.hectaria.data.model.NecesidadApi
import cl.duoc.hectaria.data.model.PrestadorApi
import cl.duoc.hectaria.data.model.UsuarioApi
import cl.duoc.hectaria.data.model.UsuarioPatch
import cl.duoc.hectaria.data.model.VinculacionApi
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("prestadores?select=*&order=id")
    suspend fun obtenerPrestadores(): List<PrestadorApi>

    @GET("usuarios?select=*")
    suspend fun login(@Query("correo") correo: String, @Query("clave") clave: String): List<UsuarioApi>

    @POST("usuarios")
    suspend fun registrar(@Body usuario: UsuarioApi)

    @PATCH("usuarios?select=correo,nombre")
    suspend fun actualizarPerfil(@Query("correo") correo: String, @Body datos: UsuarioPatch): List<UsuarioApi>

    @POST("necesidades")
    suspend fun guardarNecesidad(@Body necesidad: NecesidadApi)

    @GET("necesidades?select=*&order=fecha.desc")
    suspend fun obtenerNecesidades(@Query("correo_usuario") correo: String): List<NecesidadApi>

    @POST("vinculaciones")
    suspend fun guardarVinculacion(@Body vinculacion: VinculacionApi)

    @GET("vinculaciones?select=*&order=fecha.desc")
    suspend fun obtenerVinculaciones(@Query("correo_usuario") correo: String): List<VinculacionApi>
}
