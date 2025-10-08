package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import pe.edu.upc.bikelab3.network.Alquiler
import pe.edu.upc.bikelab3.network.Bicicleta
import pe.edu.upc.bikelab3.network.LocalJsonReader
import pe.edu.upc.bikelab3.network.Proveedor
import pe.edu.upc.bikelab3.network.ReservationManager
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager

enum class Section { INICIO, UBICACION, NOTIFICACIONES, MIS_ALQUILERES, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var section by rememberSaveable { mutableStateOf(Section.INICIO) }

    // Si no hay sesión, vuelve al login (evita NPEs en Perfil)
    if (UserSession.currentUser == null) {
        LaunchedEffect(Unit) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
        return
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
                                // Navegar a perfil según tipo de usuario
                                val profileRoute = if (user.tipo == "Arrendatario") "arrendatario-profile" else "profile"
                                navController.navigate(profileRoute)
                                scope.launch { drawerState.close() }
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar del usuario
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Gray),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_bikelab),
                                contentDescription = "Avatar",
                                modifier = Modifier.size(40.dp)
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

                // Elementos del menú
                DrawerItem(
                    label = "Inicio",
                    selected = section == Section.INICIO,
                    onClick = {
                        section = Section.INICIO
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Home, null) }
                )
                DrawerItem(
                    label = "Ubicación",
                    selected = section == Section.UBICACION,
                    onClick = {
                        section = Section.UBICACION
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.LocationOn, null) }
                )
                DrawerItem(
                    label = "Notificaciones",
                    selected = section == Section.NOTIFICACIONES,
                    onClick = {
                        section = Section.NOTIFICACIONES
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Notifications, null) }
                )
                DrawerItem(
                    label = "Mis Alquileres",
                    selected = section == Section.MIS_ALQUILERES,
                    onClick = {
                        section = Section.MIS_ALQUILERES
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.LocationOn, null) }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem(
                    label = "Cerrar Sesión",
                    selected = false,
                    onClick = {
                        UserSession.currentUser = null
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.ExitToApp, null) }
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
                                painter = painterResource(id = R.drawable.logo_bikelab),
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
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) { 
                            Icon(
                                Icons.Default.Menu, 
                                contentDescription = "Menú",
                                tint = Color.White
                            ) 
                        }
                    },
                    actions = {
                        IconButton(onClick = { 
                            // Navegar a perfil según tipo de usuario
                            val currentUser = UserSession.currentUser
                            val profileRoute = if (currentUser?.tipo == "Arrendatario") "arrendatario-profile" else "profile"
                            navController.navigate(profileRoute)
                        }) {
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
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (section) {
                    Section.INICIO -> HomeContent(navController)
                    Section.UBICACION -> LocationContent()
                    Section.NOTIFICACIONES -> {
                        LaunchedEffect(Unit) {
                            navController.navigate("notifications")
                        }
                    }
                    Section.MIS_ALQUILERES -> MisAlquileresContent()
                    Section.PERFIL -> {
                        LaunchedEffect(Unit) {
                            navController.navigate("profile")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)? = null
) {
    NavigationDrawerItem(
        label = { Text(label) },
        selected = selected,
        onClick = onClick,
        icon = icon,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}

@Composable 
private fun HomeContent(navController: NavController) {
    val context = LocalContext.current
    val todasLasBicicletas = remember { 
        LocalJsonReader.getBicicletasDisponibles(context) 
    }
    val bicicletasDisponibles = remember(todasLasBicicletas) {
        ReservationManager.getAvailableBikes(todasLasBicicletas)
    }
    
    // Obtener vehículos registrados por arrendatarios
    val vehiculosRegistrados = remember {
        VehiculoManager.getAllVehiculos()
    }
    
    // Combinar bicicletas de db.json con vehículos registrados
    val todosLosVehiculosDisponibles = remember(bicicletasDisponibles, vehiculosRegistrados) {
        bicicletasDisponibles + vehiculosRegistrados.map { vehiculo ->
            Bicicleta(
                id = vehiculo.id,
                proveedorId = vehiculo.propietarioId,
                modelo = vehiculo.marcaModelo,
                marca = vehiculo.marcaModelo.split(" ").firstOrNull() ?: "Personalizada",
                tipo = vehiculo.tipo,
                precioPorHora = vehiculo.precioPorHora.toDoubleOrNull() ?: 0.0,
                ubicacion = vehiculo.ubicacionActual,
                disponible = true,
                rating = 4.5, // Rating por defecto para vehículos registrados
                descripcion = "Vehículo registrado por arrendatario",
                imagen = "placeholder"
            )
        }
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Sección del mapa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder del mapa
            Text(
                text = "MAPA INTERACTIVO\n📍 Marcadores de vehículos verdes\n\n${todosLosVehiculosDisponibles.size} vehículos disponibles",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.DarkGray,
                fontSize = 16.sp
            )
        }
        
        // Sección de vehículos disponibles
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .border(
                    width = 2.dp,
                    color = colorResource(id = R.color.lime_green),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "VEHÍCULOS DISPONIBLES PARA ALQUILER",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Lista de vehículos (bicicletas + vehículos registrados)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(todosLosVehiculosDisponibles) { vehiculo ->
                    BikeCardFromDB(
                        bicicleta = vehiculo, 
                        context = context,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable private fun LocationContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Pantalla de Ubicación 📍") }

@Composable private fun NotificationsContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Notificaciones 📢") }

@Composable private fun MisAlquileresContent() {
    val context = LocalContext.current
    val alquileresUsuario = remember { ReservationManager.getUserRentals() }
    val todasLasBicicletas = remember { LocalJsonReader.getBicicletas(context) }
    val todosLosProveedores = remember { LocalJsonReader.getProveedores(context) }
    
    // Alquileres predefinidos para mostrar siempre
    val alquileresPredefinidos = remember {
        listOf(
            Alquiler(
                id = 1001,
                bicicletaId = 1,
                usuarioId = 1,
                fechaInicio = "2024-12-15",
                fechaFin = "2024-12-16",
                precioTotal = 240.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - Monterrico",
                ubicacionDevolucion = "UPC - Monterrico",
                notas = "Alquiler completado exitosamente"
            ),
            Alquiler(
                id = 1002,
                bicicletaId = 2,
                usuarioId = 1,
                fechaInicio = "2024-12-10",
                fechaFin = "2024-12-11",
                precioTotal = 288.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - San Miguel",
                ubicacionDevolucion = "UPC - San Miguel",
                notas = "Excelente servicio"
            )
        )
    }
    
    // Combinar alquileres predefinidos con los del usuario
    val todosLosAlquileres = alquileresPredefinidos + alquileresUsuario
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Alquileres",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(todosLosAlquileres) { alquiler ->
                AlquilerCard(
                    alquiler = alquiler,
                    bicicletas = todasLasBicicletas,
                    proveedores = todosLosProveedores
                )
            }
        }
    }
}

// Componente para mostrar cada bicicleta desde la base de datos
@Composable
private fun BikeCardFromDB(
    bicicleta: Bicicleta, 
    context: android.content.Context,
    navController: NavController
) {
    val proveedores = remember { LocalJsonReader.getProveedores(context) }
    val usuarios = remember { LocalJsonReader.getUsuarios(context) }
    
    // Buscar proveedor en db.json o usuario arrendatario
    val proveedor = proveedores.find { it.id == bicicleta.proveedorId }
    val usuarioArrendatario = usuarios.find { it.id == bicicleta.proveedorId && it.tipo == "Arrendatario" }
    
    // Determinar el nombre y apellido a mostrar
    // Si es un vehículo registrado por arrendatario, usar el nombre del arrendatario
    // Si es un vehículo de db.json, usar el nombre del proveedor
    val nombreMostrar = if (usuarioArrendatario != null) {
        usuarioArrendatario.nombre
    } else {
        proveedor?.nombre ?: "Proveedor"
    }
    
    val apellidoMostrar = if (usuarioArrendatario != null) {
        usuarioArrendatario.apellido
    } else {
        proveedor?.apellido ?: ""
    }
    
    val ubicacionProveedor = proveedor?.direccion ?: usuarioArrendatario?.direccion ?: "Ubicación no disponible"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = colorResource(id = R.color.lime_green),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de perfil (placeholder)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_bikelab),
                    contentDescription = "Profile",
                    modifier = Modifier.size(30.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Información de la bicicleta
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${nombreMostrar.uppercase()} ${apellidoMostrar.uppercase()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Text(
                    text = "★★★★ (${bicicleta.rating})",
                    fontSize = 12.sp,
                    color = Color.Yellow
                )
                
                Text(
                    text = "BICICLETA ${bicicleta.modelo}",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                
                Text(
                    text = bicicleta.ubicacion,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
                
                Text(
                    text = "S/ ${String.format("%.2f", bicicleta.precioPorHora)} POR HORA",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lime_green)
                )
            }
            
            // Botón de acción
            Button(
                onClick = { 
                    navController.navigate("rent/${bicicleta.id}")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lime_green)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = "CONOCER MÁS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// Componente para mostrar cada alquiler
@Composable
private fun AlquilerCard(
    alquiler: Alquiler,
    bicicletas: List<Bicicleta>,
    proveedores: List<Proveedor>
) {
    val bicicleta = bicicletas.find { it.id == alquiler.bicicletaId }
    val proveedor = bicicleta?.let { 
        proveedores.find { prov -> prov.id == it.proveedorId }
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bicicleta?.modelo ?: "Bicicleta no encontrada",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                // Estado del alquiler
                Box(
                    modifier = Modifier
                        .background(
                            when (alquiler.estado) {
                                "ACTIVO" -> colorResource(id = R.color.lime_green)
                                "COMPLETADO" -> Color.Blue
                                "CANCELADO" -> Color.Red
                                else -> Color.Gray
                            },
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = alquiler.estado,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Proveedor: ${proveedor?.nombre ?: "Proveedor"} ${proveedor?.apellido ?: ""}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Text(
                text = "Fecha: ${alquiler.fechaInicio} - ${alquiler.fechaFin}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Text(
                text = "Ubicación: ${alquiler.ubicacionRecogida}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: S/ ${String.format("%.2f", alquiler.precioTotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lime_green)
                )
                
                if (alquiler.estado == "ACTIVO") {
                    Button(
                        onClick = { /* Lógica para finalizar alquiler */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.lime_green)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Finalizar",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
