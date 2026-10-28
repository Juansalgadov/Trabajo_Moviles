package cl.duoc.hectaria.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.model.AppDatabase
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.obtener(application).usuarioDao()

    var correo by mutableStateOf("")
    var clave by mutableStateOf("")

    var errorCorreo by mutableStateOf("")
    var errorClave by mutableStateOf("")

    fun validarCorreo(): Boolean {
        errorCorreo = when {
            correo.isBlank() -> "Debes ingresar tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> "El correo no tiene un formato válido"
            else -> ""
        }
        return errorCorreo.isEmpty()
    }

    fun validarClave(): Boolean {
        errorClave = when {
            clave.isBlank() -> "Debes ingresar tu contraseña"
            clave.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> ""
        }
        return errorClave.isEmpty()
    }

    fun validarFormulario(): Boolean {
        val correoOk = validarCorreo()
        val claveOk = validarClave()
        return correoOk && claveOk
    }

    fun revisarSesion(alEntrar: () -> Unit) {
        viewModelScope.launch {
            if (dao.sesion() != null) {
                alEntrar()
            }
        }
    }

    fun entrar(alEntrar: () -> Unit) {
        if (!validarFormulario()) return
        viewModelScope.launch {
            val usuario = dao.buscar(correo, clave)
            if (usuario == null) {
                errorClave = "El correo o la contraseña no coinciden"
            } else {
                dao.cerrarSesiones()
                dao.abrirSesion(usuario.correo)
                alEntrar()
            }
        }
    }
}
