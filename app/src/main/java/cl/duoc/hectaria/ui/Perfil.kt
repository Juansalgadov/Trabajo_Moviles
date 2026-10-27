package cl.duoc.hectaria.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.hectaria.ui.theme.TextoSecundario
import cl.duoc.hectaria.viewmodel.PerfilViewModel

@Composable
fun Perfil(navController: NavController, viewModel: PerfilViewModel = viewModel()) {

    LaunchedEffect(Unit) {
        viewModel.cargar()
    }

    var guardado by remember { mutableStateOf(false) }
    val usuario = viewModel.usuario
    val esPrestador = usuario?.tipo == "prestador"

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "Mi perfil",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (usuario != null) {
            Text(
                text = "${usuario.nombre} · ${usuario.correo} · ${usuario.tipo}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (esPrestador) {
        Text("Servicios que ofrezco", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            viewModel.serviciosDisponibles.take(2).forEach { servicio ->
                FilterChip(
                    selected = servicio in viewModel.serviciosElegidos,
                    onClick = { viewModel.alternarServicio(servicio) },
                    label = { Text(servicio, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            viewModel.serviciosDisponibles.drop(2).forEach { servicio ->
                FilterChip(
                    selected = servicio in viewModel.serviciosElegidos,
                    onClick = { viewModel.alternarServicio(servicio) },
                    label = { Text(servicio, style = MaterialTheme.typography.labelSmall) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.cobertura,
            onValueChange = { viewModel.cobertura = it },
            label = { Text("Cobertura (comunas)") },
            isError = viewModel.errorCobertura.isNotEmpty(),
            supportingText = { if (viewModel.errorCobertura.isNotEmpty()) Text(viewModel.errorCobertura) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.capacidad,
            onValueChange = { viewModel.capacidad = it },
            label = { Text("Capacidad máxima (hectáreas)") },
            isError = viewModel.errorCapacidad.isNotEmpty(),
            supportingText = { if (viewModel.errorCapacidad.isNotEmpty()) Text(viewModel.errorCapacidad) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.disponibleDesde,
            onValueChange = { viewModel.disponibleDesde = it },
            label = { Text("Disponible desde (dd/mm/aaaa)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.experiencia,
            onValueChange = { viewModel.experiencia = it },
            label = { Text("Años de experiencia") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { if (viewModel.validarFormulario()) guardado = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar cambios")
        }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Mis vinculaciones", style = MaterialTheme.typography.titleSmall)
        if (viewModel.vinculaciones.isEmpty()) {
            Text("Todavía no tienes vinculaciones.", style = MaterialTheme.typography.bodyMedium, color = TextoSecundario)
        } else {
            viewModel.vinculaciones.forEach { vinculacion ->
                Text(
                    text = "· ${vinculacion.prestador} - ${vinculacion.servicio} (${vinculacion.cultivo})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextoSecundario
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }

        if (guardado) {
            AlertDialog(
                onDismissRequest = { guardado = false },
                title = { Text("Listo") },
                text = { Text("Los cambios de tu perfil quedaron guardados.") },
                confirmButton = {
                    Button(onClick = { guardado = false }) { Text("OK") }
                }
            )
        }
    }
}
