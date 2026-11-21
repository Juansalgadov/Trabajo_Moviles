package cl.duoc.hectaria.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.hectaria.data.remote.RetrofitInstance
import cl.duoc.hectaria.model.AppDatabase
import cl.duoc.hectaria.model.Usuario
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
            try {
                val encontrados = RetrofitInstance.api.login("eq." + correo.trim(), "eq." + clave)
                val usuario = encontrados.firstOrNull()
                if (usuario == null) {
                    errorClave = "El correo o la contraseña no coinciden"
                } else {
                    dao.cerrarSesiones()
                    dao.guardar(Usuario(usuario.correo, usuario.nombre, clave, usuario.tipo, true))
                    alEntrar()
                }
            } catch (e: Exception) {
                errorClave = "No pudimos conectarnos con el servidor"
            }
        }
    }
}
