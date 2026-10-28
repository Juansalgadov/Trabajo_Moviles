package cl.duoc.hectaria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cl.duoc.hectaria.ui.Navegacion
import cl.duoc.hectaria.ui.theme.HectariaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HectariaTheme {
                Navegacion()
            }
        }
    }
}
