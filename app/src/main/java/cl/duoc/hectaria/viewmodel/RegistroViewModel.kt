package cl.duoc.hectaria.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegistroViewModel : ViewModel() {

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
}
