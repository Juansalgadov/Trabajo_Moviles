package cl.duoc.hectaria.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cl.duoc.hectaria.model.Prestador
import cl.duoc.hectaria.repository.PrestadorRepository
import cl.duoc.hectaria.util.Notificaciones
import cl.duoc.hectaria.viewmodel.ResultadosViewModel
import cl.duoc.hectaria.ui.theme.TextoSecundario

@Composable
fun Resultados(navController: NavController, viewModel: ResultadosViewModel = viewModel()) {

    val context = LocalContext.current

    val permisoNotificaciones = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= 33) {
            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        viewModel.cargar()
    }

    val necesidad = viewModel.necesidad
    var orden by remember { mutableStateOf("compatibilidad") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Prestadores compatibles",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        if (necesidad == null) {
            Spacer(modifier = Modifier.height(24.dp))
            if (viewModel.cargado) {
                Text("Todavía no publicas ninguna necesidad.", color = TextoSecundario)
            }
            return@Column
        }

        val resultados = when (orden) {
            "experiencia" -> PrestadorRepository.buscarCompatibles(necesidad).sortedByDescending { it.first.experiencia }
            "evaluación" -> PrestadorRepository.buscarCompatibles(necesidad).sortedByDescending { it.first.evaluacion }
            else -> PrestadorRepository.buscarCompatibles(necesidad)
        }

        LaunchedEffect(resultados.size) {
            if (resultados.isNotEmpty()) {
                Notificaciones.avisarResultados(context, resultados.size)
            }
        }

        Text(
            text = "${resultados.size} resultados para ${necesidad.servicio} - ${necesidad.cultivo}",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("compatibilidad", "experiencia", "evaluación").forEach { opcion ->
                FilterChip(
                    selected = orden == opcion,
                    onClick = { orden = opcion },
                    label = { Text(opcion) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (resultados.isEmpty()) {
            Text(
                text = "No encontramos prestadores que calcen con tu necesidad. Intenta con otra zona u otro servicio.",
                color = TextoSecundario
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(resultados) { _, resultado ->
                    TarjetaPrestador(resultado.first, resultado.second) {
                        navController.navigate("detalle/" + resultado.first.id)
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaPrestador(prestador: Prestador, compatibilidad: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = prestador.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$compatibilidad%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "${prestador.zona} · ${prestador.experiencia} años · ${if (prestador.disponible) "Disponible" else "Ocupado"}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Text(
                text = prestador.servicios.joinToString(", "),
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { compatibilidad / 100f },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
