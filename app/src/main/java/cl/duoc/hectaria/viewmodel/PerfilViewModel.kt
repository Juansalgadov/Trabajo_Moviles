package cl.duoc.hectaria.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.data.model.PublicarPrestador
import cl.duoc.hectaria.data.model.UsuarioPatch
import cl.duoc.hectaria.data.model.VinculacionApi
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Usuario
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val base = AppDatabase.obtener(application)

    val serviciosDisponibles = listOf("Fumigación con dron", "Aplicación foliar", "Monitoreo de cultivo", "Siembra con dron")

    var usuario by mutableStateOf<Usuario?>(null)
    var nombre by mutableStateOf("")
    var errorNombre by mutableStateOf("")
    var vinculaciones by mutableStateOf(listOf<VinculacionApi>())

    var serviciosElegidos by mutableStateOf(listOf("Fumigación con dron"))
    var cobertura by mutableStateOf("")
    var capacidad by mutableStateOf("")
    var disponibleDesde by mutableStateOf("")
    var experiencia by mutableStateOf("")

    var errorCobertura by mutableStateOf("")
    var errorCapacidad by mutableStateOf("")

    fun cargar() {
        viewModelScope.launch {
            usuario = base.usuarioDao().sesion()
            nombre = usuario?.nombre ?: ""
            // si es prestador, traigo sus datos del catalogo para llenar el formulario
            if (usuario?.tipo == "prestador") {
                try {
                    val mios = RetrofitInstance.api.obtenerPrestadores().find { it.correo == usuario?.correo }
                    if (mios != null) {
                        cobertura = mios.zona
                        capacidad = mios.capacidad.toString()
                        experiencia = mios.experiencia.toString()
                        serviciosElegidos = mios.servicios.split(",").map { it.trim() }
                    }
                } catch (e: Exception) {
                    // sin internet el formulario queda vacio y ya
                }
            }
            vinculaciones = try {
                RetrofitInstance.api.obtenerVinculaciones("eq." + (usuario?.correo ?: ""))
            } catch (e: Exception) {
                base.vinculacionDao().obtenerTodas().map {
                    VinculacionApi(
                        correo_usuario = "",
                        prestador_id = 0,
                        prestador_nombre = it.prestador,
                        compatibilidad = 0,
                        servicio = it.servicio,
                        cultivo = it.cultivo
                    )
                }
            }
        }
    }

    fun guardarCambios(alGuardar: () -> Unit) {
        val actual = usuario ?: return
        viewModelScope.launch {
            try {
                RetrofitInstance.api.actualizarPerfil(
                    "eq." + actual.correo,
                    UsuarioPatch(
                        nombre = nombre.ifBlank { null },
                        cobertura = if (actual.tipo == "prestador" && cobertura.isNotBlank()) cobertura else null,
                        capacidad = if (actual.tipo == "prestador") capacidad.toIntOrNull() else null,
                        experiencia = if (actual.tipo == "prestador") experiencia.toIntOrNull() else null
                    ),
                    "return=representation"
                )
                // si es prestador, tambien se publica en el catalogo para salir en las busquedas
                if (actual.tipo == "prestador") {
                    RetrofitInstance.api.publicarPrestador(
                        PublicarPrestador(
                            correo = actual.correo,
                            nombre = nombre.ifBlank { actual.nombre },
                            zona = cobertura,
                            servicios = serviciosElegidos.joinToString(", "),
                            cultivos = "",
                            capacidad = capacidad.toIntOrNull() ?: 0,
                            experiencia = experiencia.toIntOrNull() ?: 0,
                            disponible = true,
                            evaluacion = 4.0
                        ),
                        "resolution=merge-duplicates"
                    )
                }
                base.usuarioDao().guardar(actual.copy(nombre = nombre.ifBlank { actual.nombre }))
            } catch (e: Exception) {
                // si falla la api igual avisamos que quedo local
            }
            alGuardar()
        }
    }

    fun alternarServicio(servicio: String) {
        serviciosElegidos = if (servicio in serviciosElegidos) {
            serviciosElegidos - servicio
        } else {
            serviciosElegidos + servicio
        }
    }

    fun validarFormulario(): Boolean {
        errorNombre = if (nombre.isBlank()) "Debes ingresar tu nombre" else ""
        errorCobertura = if (cobertura.isBlank()) "Debes ingresar tu cobertura" else ""
        val hectareas = capacidad.toIntOrNull()
        errorCapacidad = when {
            capacidad.isBlank() -> "Debes ingresar tu capacidad"
            hectareas == null || hectareas <= 0 -> "La capacidad debe ser mayor a 0"
            else -> ""
        }
        return errorNombre.isEmpty() && errorCobertura.isEmpty() && errorCapacidad.isEmpty()
    }

    fun cerrarSesion(alSalir: () -> Unit) {
        viewModelScope.launch {
            base.usuarioDao().cerrarSesiones()
            alSalir()
        }
    }
}
