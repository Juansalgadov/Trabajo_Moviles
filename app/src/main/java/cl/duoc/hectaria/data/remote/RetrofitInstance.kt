package cl.duoc.hectaria.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val URL_BASE = "https://kfnomwzumynnyowetxim.supabase.co/rest/v1/"
    private const val LLAVE = "sb_publishable_f_yVXKWv9PKGDs_DBK1cvA_MAAxHAcj"

    private val cliente = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val pedido = cadena.request().newBuilder()
                .addHeader("apikey", LLAVE)
                .addHeader("Authorization", "Bearer $LLAVE")
                .build()
            cadena.proceed(pedido)
        }
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
