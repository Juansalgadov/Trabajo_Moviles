package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Necesidad
import kotlinx.coroutines.launch

class InicioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtener(application).necesidadDao()

    var necesidades by mutableStateOf(listOf<Necesidad>())

    private val ejemplos = listOf(
        Necesidad(cultivo = "Maíz", servicio = "Fumigación con dron", superficie = 12, zona = "Linares, Maule", fecha = "12/11/2026", estado = "activa"),
        Necesidad(cultivo = "Trigo", servicio = "Aplicación foliar", superficie = 30, zona = "Parral, Maule", fecha = "20/11/2026", estado = "en revisión"),
        Necesidad(cultivo = "Arándanos", servicio = "Monitoreo de cultivo", superficie = 8, zona = "San Javier, Maule", fecha = "05/10/2026", estado = "cerrada")
    )

    fun cargar() {
        viewModelScope.launch {
            if (dao.contar() == 0) {
                ejemplos.forEach { dao.insertar(it) }
            }
            necesidades = dao.obtenerTodas()
        }
    }
}
