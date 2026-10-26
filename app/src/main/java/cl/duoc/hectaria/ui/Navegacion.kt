package cl.duoc.hectaria.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun Navegacion() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    Scaffold(
        bottomBar = {
            if (rutaActual == "inicio" || rutaActual == "perfil") {
                BarraInferior(navController, rutaActual)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("login") { Login(navController) }
            composable("registro") { Registro(navController) }
            composable("inicio") { Inicio(navController) }
            composable("nueva_necesidad") { NuevaNecesidad(navController) }
            composable("resultados") { Resultados(navController) }
            composable("perfil") { Perfil(navController) }
        }
    }
}

@Composable
fun BarraInferior(navController: NavController, rutaActual: String) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == "inicio",
            onClick = { navController.navigate("inicio") },
            icon = { Icon(Icons.Default.List, contentDescription = null) },
            label = { Text("Solicitudes") }
        )
        NavigationBarItem(
            selected = rutaActual == "perfil",
            onClick = { navController.navigate("perfil") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") }
        )
    }
}
