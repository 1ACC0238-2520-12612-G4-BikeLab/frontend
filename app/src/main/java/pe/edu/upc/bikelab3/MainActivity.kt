package pe.edu.upc.bikelab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.bikelab3.screens.HomeScreen
import pe.edu.upc.bikelab3.screens.LoginScreen
import pe.edu.upc.bikelab3.screens.RegisterScreen
import pe.edu.upc.bikelab3.ui.theme.BikeLab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BikeLab3Theme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "login") {
                    composable("login") { LoginScreen(nav) }
                    composable("register") { RegisterScreen(nav) }
                    composable("home") { HomeScreen(nav) } // <- dentro vive el Drawer y todas las secciones
                }
            }
        }
    }
}
