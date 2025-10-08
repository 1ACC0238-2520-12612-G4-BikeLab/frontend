package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre completo") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(edad, { edad = it }, label = { Text("Edad") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(tipo, { tipo = it }, label = { Text("Tipo de usuario (Ciclista o Arrendatario)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(usuario, { usuario = it }, label = { Text("Usuario") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(contrasena, { contrasena = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("register") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}
