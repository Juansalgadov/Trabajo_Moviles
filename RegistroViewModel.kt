package cl.duoc.hectaria.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.data.model.UsuarioApi
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Usuario
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegistroViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtener(application).usuarioDao()

    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var clave by mutableStateOf("")
    var confirmarClave by mutableStateOf("")
    var tipoUsuario by mutableStateOf("productor")

    var errorNombre by mutableStateOf("")
    var errorCorreo by mutableStateOf("")
    var errorClave by mutableStateOf("")
    var errorConfirmar by mutableStateOf("")

    fun validarFormulario(): Boolean {
        errorNombre = if (nombre.isBlank()) "Debes ingresar tu nombre" else ""
        errorCorreo = when {
            correo.isBlank() -> "Debes ingresar tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "El correo no tiene un formato válido"
            else -> ""
        }
        errorClave = when {
            clave.isBlank() -> "Debes ingresar una contraseña"
            clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> ""
        }
        errorConfirmar = if (confirmarClave != clave) "Las contraseñas no coinciden" else ""
        return errorNombre.isEmpty() && errorCorreo.isEmpty() &&
                errorClave.isEmpty() && errorConfirmar.isEmpty()
    }

    fun registrar(alRegistrar: () -> Unit) {
        if (!validarFormulario()) return
        viewModelScope.launch {
            try {
                RetrofitInstance.api.registrar(
                    UsuarioApi(correo = correo.trim(), nombre = nombre, clave = clave, tipo = tipoUsuario)
                )
                dao.cerrarSesiones()
                dao.guardar(Usuario(correo, nombre, clave, tipoUsuario, true))
                alRegistrar()
            } catch (e: HttpException) {
                if (e.code() == 409) errorCorreo = "Ese correo ya está registrado" else errorCorreo = "No pudimos crear tu cuenta"
            } catch (e: Exception) {
                errorCorreo = "No pudimos conectarnos con el servidor"
            }
        }
    }
}
