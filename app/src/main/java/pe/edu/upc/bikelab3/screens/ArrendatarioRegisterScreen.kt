package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrendatarioRegisterScreen(navController: NavController) {
    val context = LocalContext.current
        var nombreCompleto by remember { mutableStateOf("") }
        var celular by remember { mutableStateOf("") }
        var dniRuc by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    Image(
                        painter = painterResource(id = R.drawable.bikelablogo),
                        contentDescription = "Logo BikeLab",
                        modifier = Modifier.size(32.dp)
                    )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "BIKELAB",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.lime_green)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("user-type-selection") }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.LightGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título "REGISTRO"
            Text(
                text = "REGISTRO",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.lime_green),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Formulario
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nombre completo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nombre completo",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.width(140.dp)
                    )
                    
                    OutlinedTextField(
                        value = nombreCompleto,
                        onValueChange = { nombreCompleto = it },
                        placeholder = { Text("Nombre, apellido", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = colorResource(id = R.color.lime_green),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = colorResource(id = R.color.lime_green)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Celular
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Celular",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.width(140.dp)
                    )
                    
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Código de país
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(48.dp)
                                .background(
                                    colorResource(id = R.color.lime_green),
                                    RoundedCornerShape(8.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+51",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        // Número de celular
                        OutlinedTextField(
                            value = celular,
                            onValueChange = { celular = it },
                            placeholder = { Text("", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.lime_green),
                                unfocusedBorderColor = colorResource(id = R.color.lime_green),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = colorResource(id = R.color.lime_green)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                // DNI / RUC
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DNI / RUC",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.width(140.dp)
                    )
                    
                    OutlinedTextField(
                        value = dniRuc,
                        onValueChange = { dniRuc = it },
                        placeholder = { Text("", color = Color.Gray) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = colorResource(id = R.color.lime_green),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = colorResource(id = R.color.lime_green)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón "Siguiente"
            Button(
                onClick = {
                    if (nombreCompleto.isNotEmpty() && celular.isNotEmpty() && dniRuc.isNotEmpty()) {
                        // Separar nombre y apellido
                        val partesNombre = nombreCompleto.split(" ")
                        val nombre = partesNombre.firstOrNull() ?: ""
                        val apellido = partesNombre.drop(1).joinToString(" ")
                        
                        // Actualizar usuario existente con datos adicionales
                        val currentUser = UserSession.currentUser
                        if (currentUser != null) {
                            val usuarioActualizado = currentUser.copy(
                                nombre = nombre,
                                apellido = apellido,
                                numero = "+51 $celular",
                                codigo = dniRuc,
                                tipo = "Arrendatario"
                            )
                            
                            UserSession.currentUser = usuarioActualizado
                            navController.navigate("arrendatario-home") {
                                popUpTo("arrendatario-register") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lime_green)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Siguiente",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
