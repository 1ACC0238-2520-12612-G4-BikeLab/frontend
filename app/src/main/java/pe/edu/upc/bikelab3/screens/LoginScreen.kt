package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.LocalJsonReader
import pe.edu.upc.bikelab3.network.ReservationManager
import pe.edu.upc.bikelab3.network.UserSession

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barra lateral verde
        Box(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
                .background(colorResource(id = R.color.lime_green))
        )
        
        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Contenido principal centrado
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo y título
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_bikelab),
                        contentDescription = "Logo BikeLab",
                        modifier = Modifier.size(48.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Text(
                        text = "BIKELAB",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.dark_gray),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Mensaje de bienvenida
                Text(
                    text = "Bienvenido a BikeLab!!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_gray),
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Formulario de login
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Campo Usuario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuario",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.dark_gray),
                            modifier = Modifier.width(100.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            placeholder = { Text("email", color = colorResource(id = R.color.light_gray)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.light_green),
                                unfocusedBorderColor = colorResource(id = R.color.light_green),
                                cursorColor = colorResource(id = R.color.lime_green)
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Campo Contraseña
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Contraseña",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(id = R.color.dark_gray),
                            modifier = Modifier.width(100.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("c****", color = colorResource(id = R.color.light_gray)) },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp)),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.light_green),
                                unfocusedBorderColor = colorResource(id = R.color.light_green),
                                cursorColor = colorResource(id = R.color.lime_green)
                            )
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Enlace "Olvidé mi contraseña"
                    Text(
                        text = "Olvidé mi contraseña",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.dark_gray),
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.align(Alignment.End)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Botón Iniciar Sesión
                    Button(
                        onClick = {
                            // Validar que los campos no estén vacíos
                            if (username.isEmpty() || password.isEmpty()) {
                                error = "Por favor completa todos los campos"
                                return@Button
                            }
                            
                            // Buscar usuario solo en db.json
                            val user = LocalJsonReader.getUsuarios(context).find { 
                                it.usuario == username && it.contrasena == password 
                            }

                            if (user != null) {
                                error = ""
                                UserSession.currentUser = user
                                ReservationManager.clearReservations()
                                ReservationManager.clearRentals()
                                
                                // Ir al home correcto según el tipo del usuario
                                val homeRoute = if (user.tipo == "Arrendatario") "arrendatario-home" else "home"
                                navController.navigate(homeRoute) {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                error = "Usuario o contraseña incorrectos"
                            }
                        },
                        enabled = username.isNotEmpty() && password.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(4.dp, RoundedCornerShape(8.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (username.isNotEmpty() && password.isNotEmpty()) 
                                colorResource(id = R.color.lime_green) 
                            else 
                                Color.Gray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    
                    
                    // Mensaje de error
                    if (error.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}