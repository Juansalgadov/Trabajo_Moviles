package cl.duoc.hectaria.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

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
}
