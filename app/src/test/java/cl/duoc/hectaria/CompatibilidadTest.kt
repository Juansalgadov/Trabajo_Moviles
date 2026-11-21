package cl.duoc.hectaria

import cl.duoc.hectaria.model.Necesidad
import cl.duoc.hectaria.model.Prestador
import cl.duoc.hectaria.repository.PrestadorRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CompatibilidadTest {

    private val necesidad = Necesidad(
        cultivo = "Maíz",
        servicio = "Fumigación con dron",
        superficie = 50,
        zona = "Linares",
        fecha = "21/11/2026",
        estado = "activa"
    )

    private fun prestador(
        zona: String = "Linares",
        servicios: List<String> = listOf("Fumigación con dron"),
        cultivos: List<String> = listOf("Maíz"),
        capacidad: Int = 100,
        disponible: Boolean = true
    ) = Prestador(1, "Prestador de prueba", zona, servicios, cultivos, capacidad, 5, disponible, 4.5)

    @Test
    fun coincideTodoCuandoCalzaEnTodo() {
        assertEquals(100, PrestadorRepository.calcularCompatibilidad(prestador(), necesidad))
    }

    @Test
    fun pierdePuntosSiEstaOcupado() {
        assertEquals(90, PrestadorRepository.calcularCompatibilidad(prestador(disponible = false), necesidad))
    }

    @Test
    fun pierdePuntosSiLaCapacidadNoAlcanza() {
        assertEquals(90, PrestadorRepository.calcularCompatibilidad(prestador(capacidad = 10), necesidad))
    }

    @Test
    fun noSumaSiTrabajaEnOtraZona() {
        assertEquals(70, PrestadorRepository.calcularCompatibilidad(prestador(zona = "Chillán"), necesidad))
    }

    @Test
    fun dejaFueraALosQueNoCalzanNada() {
        PrestadorRepository.prestadores = listOf(
            prestador(),
            prestador(zona = "Lejano", servicios = listOf("Otro servicio"), cultivos = listOf("Otro cultivo"), disponible = false)
        )
        val resultados = PrestadorRepository.buscarCompatibles(necesidad)
        assertEquals(1, resultados.size)
        assertEquals(100, resultados.first().second)
        assertTrue(resultados.first().second >= resultados.last().second)
    }
}
