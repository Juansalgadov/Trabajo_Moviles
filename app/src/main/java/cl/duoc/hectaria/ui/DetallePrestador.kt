package cl.duoc.hectaria.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.duoc.hectaria.R
import cl.duoc.hectaria.repository.NecesidadRepository
import cl.duoc.hectaria.repository.PrestadorRepository
import cl.duoc.hectaria.ui.theme.TextoSecundario
import cl.duoc.hectaria.ui.theme.VerdeClaro

@Composable
fun DetallePrestador(navController: NavController, prestadorId: Int) {

    val prestador = PrestadorRepository.buscarPorId(prestadorId)
    val necesidad = NecesidadRepository.ultima()

    if (prestador == null) {
        Text("No encontramos al prestador.", modifier = Modifier.padding(24.dp))
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())
    ) {

        Image(
            painter = painterResource(id = R.drawable.equipo_trabajo),
            contentDescription = "Foto del equipo de trabajo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = prestador.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            if (necesidad != null) {
                Text(
                    text = "${PrestadorRepository.calcularCompatibilidad(prestador, necesidad)}%",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Servicios: ${prestador.servicios.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
        Text("Cultivos que trabaja: ${prestador.cultivos.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
        Text("Cobertura: ${prestador.zona}", style = MaterialTheme.typography.bodyMedium)
        Text("Capacidad: hasta ${prestador.capacidad} hectáreas", style = MaterialTheme.typography.bodyMedium)
        Text("Experiencia: ${prestador.experiencia} años", style = MaterialTheme.typography.bodyMedium)
        Text("Evaluación: ${prestador.evaluacion} de 5.0", style = MaterialTheme.typography.bodyMedium)
        Text(
            text = if (prestador.disponible) "Disponible ahora" else "Ocupado por el momento",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (necesidad != null) {
            Surface(shape = RoundedCornerShape(12.dp), color = VerdeClaro) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "¿Por qué aparece este prestador?",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val motivos = mutableListOf<String>()
                    if (necesidad.zona.contains(prestador.zona, ignoreCase = true)) motivos.add("trabaja en tu zona")
                    if (necesidad.servicio in prestador.servicios) motivos.add("ofrece el servicio que pediste")
                    if (necesidad.cultivo in prestador.cultivos) motivos.add("conoce tu cultivo")
                    if (prestador.capacidad >= necesidad.superficie) motivos.add("le alcanza la capacidad")
                    if (prestador.disponible) motivos.add("está disponible")
                    Text(
                        text = if (motivos.isEmpty()) "Calza parcialmente con lo que pediste." else motivos.joinToString(", ").replaceFirstChar { it.uppercase() } + ".",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Me interesa este prestador")
        }
    }
}
