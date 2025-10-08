package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.LocalJsonReader

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Logo de la app
            Image(
                painter = painterResource(id = R.drawable.logo_bikelab),
                contentDescription = "Logo BikeLab",
                modifier = Modifier
                    .height(100.dp)
                    .padding(bottom = 16.dp)
            )

            Text(
                text = "Bienvenido a BikeLab!",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val usuarios = LocalJsonReader.getUsuarios(context)
                    val user = usuarios.find { it.usuario == username && it.contrasena == password }

                    if (user != null) {
                        error = ""
                        pe.edu.upc.bikelab3.network.UserSession.currentUser = user // ✅ Guardar usuario
                        navController.navigate("home") {     // ✅ lleva al Home con menú
                            popUpTo("login") { inclusive = true }
                        }

                    } else {
                        error = "Usuario o contraseña incorrectos"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Iniciar Sesión", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate("register") }) {
                Text("¿No tienes cuenta? Crear una cuenta")
            }

            if (error.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(error, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
