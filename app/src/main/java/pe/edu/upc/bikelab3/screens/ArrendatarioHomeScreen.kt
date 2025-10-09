package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrendatarioHomeScreen(navController: NavController) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedVehicleType by remember { mutableStateOf("") }
    
    // Variables para el formulario
    var marcaModelo by remember { mutableStateOf("") }
    var precioHora by remember { mutableStateOf("") }
    var precioDia by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    // Si no hay sesión, vuelve al login
    if (UserSession.currentUser == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("arrendatario-home") { inclusive = true }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                // Perfil del usuario en la parte superior
                UserSession.currentUser?.let { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {
                                navController.navigate("arrendatario-profile")
                                scope.launch { drawerState.close() }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar del usuario
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    colorResource(id = R.color.lime_green),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.nombre.first().toString().uppercase(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = "${user.nombre.uppercase()} ${user.apellido.uppercase()}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = user.correo,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                    
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }

                // Opciones del menú
                ListItem(
                    modifier = Modifier.clickable { scope.launch { drawerState.close() } },
                    headlineContent = {
                        Text(
                            text = "Inicio",
                            color = colorResource(id = R.color.lime_green),
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                
                ListItem(
                    modifier = Modifier.clickable { 
                        navController.navigate("arrendatario-mis-vehiculos")
                        scope.launch { drawerState.close() } 
                    },
                    headlineContent = {
                        Text(
                            text = "Mis Vehículos",
                            color = Color.Black
                        )
                    }
                )
                
                ListItem(
                    modifier = Modifier.clickable { scope.launch { drawerState.close() } },
                    headlineContent = {
                        Text(
                            text = "Estadísticas",
                            color = Color.Black
                        )
                    }
                )
                
                    ListItem(
                        modifier = Modifier.clickable {
                            navController.navigate("arrendatario-notifications")
                            scope.launch { drawerState.close() }
                        },
                        headlineContent = {
                            Text(
                                text = "Notificaciones",
                                color = Color.Black
                            )
                        }
                    )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                ListItem(
                    modifier = Modifier.clickable {
                        UserSession.currentUser = null
                        navController.navigate("login") {
                            popUpTo("arrendatario-home") { inclusive = true }
                        }
                    },
                    headlineContent = {
                        Text(
                            text = "Cerrar Sesión",
                            color = Color.Black
                        )
                    }
                )
            }
        }
    ) {
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
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            ) 
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("arrendatario-profile") }) {
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
                // Título "AGREGAR VEHÍCULO"
                Text(
                    text = "AGREGAR VEHÍCULO",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Selección de tipo de vehículo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Botón Scooter
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clickable { selectedVehicleType = "Scooter" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedVehicleType == "Scooter") 
                                colorResource(id = R.color.lime_green) 
                            else 
                                Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "SCOOTER",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                        }
                    }

                    // Botón Bicicleta
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp)
                            .clickable { selectedVehicleType = "Bicicleta" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedVehicleType == "Bicicleta") 
                                colorResource(id = R.color.lime_green) 
                            else 
                                Color.LightGray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "BICICLETA",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Línea separadora
                Divider(
                    color = Color.White,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Título "DETALLES"
                Text(
                    text = "DETALLES",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Formulario de detalles
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Marca / Modelo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Marca / Modelo",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(140.dp)
                        )
                        
                        OutlinedTextField(
                            value = marcaModelo,
                            onValueChange = { marcaModelo = it },
                            placeholder = { Text("", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Precio por hora
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Precio por hora",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(140.dp)
                        )
                        
                        OutlinedTextField(
                            value = precioHora,
                            onValueChange = { precioHora = it },
                            placeholder = { Text("", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Precio por día
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Precio por día",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(140.dp)
                        )
                        
                        OutlinedTextField(
                            value = precioDia,
                            onValueChange = { precioDia = it },
                            placeholder = { Text("", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Ubicación actual
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ubicación actual",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.width(140.dp)
                        )
                        
                        OutlinedTextField(
                            value = ubicacion,
                            onValueChange = { ubicacion = it },
                            placeholder = { Text("", color = Color.Gray) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón "GUARDAR"
                Button(
                    onClick = {
                        if (selectedVehicleType.isNotEmpty() && marcaModelo.isNotEmpty() && 
                            precioHora.isNotEmpty() && precioDia.isNotEmpty() && ubicacion.isNotEmpty()) {
                            // Crear y guardar el vehículo
                            val vehiculo = VehiculoManager.crearVehiculo(
                                tipo = selectedVehicleType,
                                marcaModelo = marcaModelo,
                                precioPorHora = precioHora,
                                precioPorDia = precioDia,
                                ubicacionActual = ubicacion,
                                propietarioId = UserSession.currentUser?.id ?: 0
                            )
                            VehiculoManager.agregarVehiculo(vehiculo)
                            
                            // Limpiar el formulario
                            selectedVehicleType = ""
                            marcaModelo = ""
                            precioHora = ""
                            precioDia = ""
                            ubicacion = ""
                            
                            // Navegar a Mis Vehículos
                            navController.navigate("arrendatario-mis-vehiculos")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "GUARDAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
