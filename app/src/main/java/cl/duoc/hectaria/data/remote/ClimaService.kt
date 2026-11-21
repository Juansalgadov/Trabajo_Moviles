package cl.duoc.hectaria.data.remote

import cl.duoc.hectaria.data.model.ClimaRespuesta
import cl.duoc.hectaria.data.model.LugaresRespuesta
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApi {
    @GET("v1/forecast")
    suspend fun verClima(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("current") actual: String = "temperature_2m,precipitation,wind_speed_10m"
    ): ClimaRespuesta
}

interface GeoApi {
    @GET("v1/search")
    suspend fun buscarLugar(
        @Query("name") nombre: String,
        @Query("count") cantidad: Int = 10,
        @Query("language") idioma: String = "es"
    ): LugaresRespuesta
}

object ClimaInstance {
    val clima: ClimaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClimaApi::class.java)
    }

    val geo: GeoApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://geocoding-api.open-meteo.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoApi::class.java)
    }
}
