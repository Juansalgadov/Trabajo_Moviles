package cl.duoc.hectaria

import cl.duoc.hectaria.repository.ClimaRepository
import org.junit.Assert.assertTrue
import org.junit.Test

class ClimaTest {

    @Test
    fun buenClimaCuandoNoHayLluviaNiViento() {
        assertTrue(ClimaRepository.evaluar(0.0, 5.0).contains("Buen clima"))
    }

    @Test
    fun malClimaSiHayLluvia() {
        assertTrue(ClimaRepository.evaluar(5.0, 8.0).contains("no se recomienda"))
    }

    @Test
    fun malClimaSiElVientoEsMuyFuerte() {
        assertTrue(ClimaRepository.evaluar(0.0, 30.0).contains("no se recomienda"))
    }

    @Test
    fun avisaCuandoElVientoEsFuertePeroSePuede() {
        assertTrue(ClimaRepository.evaluar(0.0, 15.0).contains("Cuidado"))
    }

    @Test
    fun mensajeCuandoNoHayDatos() {
        assertTrue(ClimaRepository.evaluar(null, null).contains("No hay datos"))
    }
}
