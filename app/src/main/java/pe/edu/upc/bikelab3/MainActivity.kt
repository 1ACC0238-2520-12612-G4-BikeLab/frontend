package pe.edu.upc.bikelab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.bikelab3.screens.HomeScreen
import pe.edu.upc.bikelab3.screens.LoginScreen
import pe.edu.upc.bikelab3.screens.NotificationsScreen
import pe.edu.upc.bikelab3.screens.ArrendatarioHomeScreen
import pe.edu.upc.bikelab3.screens.ArrendatarioProfileScreen
import pe.edu.upc.bikelab3.screens.ArrendatarioRegisterScreen
import pe.edu.upc.bikelab3.screens.ArrendatarioNotificationsScreen
import pe.edu.upc.bikelab3.screens.ArrendatarioMisVehiculosScreen
import pe.edu.upc.bikelab3.screens.ProfileScreen
import pe.edu.upc.bikelab3.screens.RentScreen
import pe.edu.upc.bikelab3.ui.theme.BikeLab3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BikeLab3Theme {
                val nav = rememberNavController()
                NavHost(navController = nav, startDestination = "login") {
                    composable("login") { LoginScreen(nav) }
                    composable("arrendatario-register") { ArrendatarioRegisterScreen(nav) }
                    composable("home") { HomeScreen(nav) } // <- dentro vive el Drawer y todas las secciones
                    composable("arrendatario-home") { ArrendatarioHomeScreen(nav) }
                    composable("rent/{bicicletaId}") { backStackEntry ->
                        val bicicletaId = backStackEntry.arguments?.getString("bicicletaId")?.toIntOrNull() ?: 1
                        RentScreen(nav, bicicletaId)
                    }
                    composable("profile") { ProfileScreen(nav) }
                    composable("arrendatario-profile") { ArrendatarioProfileScreen(nav) }
                    composable("notifications") { NotificationsScreen(nav) }
                    composable("arrendatario-notifications") { ArrendatarioNotificationsScreen(nav) }
                    composable("arrendatario-mis-vehiculos") { ArrendatarioMisVehiculosScreen(nav) }
                }
            }
        }
    }
}
