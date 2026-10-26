package cl.duoc.hectaria.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.repository.NecesidadRepository
import cl.duoc.hectaria.ui.theme.NaranjoPendiente
import cl.duoc.hectaria.ui.theme.TextoSecundario
import cl.duoc.hectaria.ui.theme.VerdeClaro
import cl.duoc.hectaria.ui.theme.VerdePrincipal

@Composable
fun Inicio(navController: NavController) {

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            text = "Mis solicitudes",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(NecesidadRepository.necesidades) { necesidad ->
                TarjetaNecesidad(necesidad)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("nueva_necesidad") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publicar una necesidad")
        }
    }
}

@Composable
fun TarjetaNecesidad(necesidad: Necesidad) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${necesidad.cultivo} - ${necesidad.servicio}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                EtiquetaEstado(necesidad.estado)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${necesidad.superficie} hectáreas · ${necesidad.zona}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario
            )
            Text(
                text = "Fecha estimada: ${necesidad.fecha}",
                style = MaterialTheme.typography.bodySmall,
                color = TextoSecundario
            )
        }
    }
}

@Composable
fun EtiquetaEstado(estado: String) {
    val fondo = when (estado) {
        "activa" -> VerdeClaro
        "en revisión" -> Color(0xFFFFF3E0)
        else -> Color(0xFFEEEEEE)
    }
    val colorTexto = when (estado) {
        "activa" -> VerdePrincipal
        "en revisión" -> NaranjoPendiente
        else -> TextoSecundario
    }
    Surface(shape = RoundedCornerShape(12.dp), color = fondo) {
        Text(
            text = estado,
            style = MaterialTheme.typography.labelMedium,
            color = colorTexto,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
