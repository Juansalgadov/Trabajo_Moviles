package cl.duoc.hectaria.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.hectaria.viewmodel.NecesidadViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun NuevaNecesidad(navController: NavController, viewModel: NecesidadViewModel = viewModel()) {

    val context = LocalContext.current

    val permisoUbicacion = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            obtenerZonaGps(context) { zona -> viewModel.zona = zona }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Nueva necesidad",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        CampoDesplegable(
            etiqueta = "Cultivo",
            valor = viewModel.cultivo,
            opciones = viewModel.cultivos,
            error = viewModel.errorCultivo,
            onSeleccion = { viewModel.cultivo = it }
        )

        CampoDesplegable(
            etiqueta = "Servicio",
            valor = viewModel.servicio,
            opciones = viewModel.servicios,
            error = viewModel.errorServicio,
            onSeleccion = { viewModel.servicio = it }
        )

        OutlinedTextField(
            value = viewModel.superficie,
            onValueChange = { viewModel.superficie = it },
            label = { Text("Superficie (hectáreas)") },
            isError = viewModel.errorSuperficie.isNotEmpty(),
            supportingText = {
                if (viewModel.errorSuperficie.isNotEmpty()) Text(viewModel.errorSuperficie)
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row {
            OutlinedTextField(
                value = viewModel.zona,
                onValueChange = { viewModel.zona = it },
                label = { Text("Zona del predio") },
                isError = viewModel.errorZona.isNotEmpty(),
                supportingText = {
                    if (viewModel.errorZona.isNotEmpty()) Text(viewModel.errorZona)
                    else Text("Puedes usar el GPS o escribirla a mano")
                },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { permisoUbicacion.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("GPS")
            }
        }

        OutlinedTextField(
            value = viewModel.fecha,
            onValueChange = { viewModel.fecha = it },
            label = { Text("Fecha estimada (dd/mm/aaaa)") },
            isError = viewModel.errorFecha.isNotEmpty(),
            supportingText = {
                if (viewModel.errorFecha.isNotEmpty()) Text(viewModel.errorFecha)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (viewModel.validarFormulario()) {
                    viewModel.guardar {
                        navController.navigate("resultados")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar prestadores")
        }
    }
}

@Composable
fun CampoDesplegable(
    etiqueta: String,
    valor: String,
    opciones: List<String>,
    error: String,
    onSeleccion: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            label = { Text(etiqueta) },
            trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
            isError = error.isNotEmpty(),
            supportingText = { if (error.isNotEmpty()) Text(error) },
            modifier = Modifier.fillMaxWidth()
        )
        Box(modifier = Modifier.matchParentSize().clickable { expandido = true })
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onSeleccion(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}

@SuppressLint("MissingPermission")
fun obtenerZonaGps(context: android.content.Context, onZona: (String) -> Unit) {
    val cliente = LocationServices.getFusedLocationProviderClient(context)
    cliente.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, CancellationTokenSource().token)
        .addOnSuccessListener { ubicacion ->
            if (ubicacion != null) {
                onZona("Lat %.4f, Lon %.4f".format(ubicacion.latitude, ubicacion.longitude))
            }
        }
}
