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
import kotlinx.coroutines.delay
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
                                painter = painterResource(id = R.drawable.bikelablogo),
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
                    Section.MIS_ALQUILERES -> MisAlquileresContent(navController)
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
        Image(
            painter = painterResource(id = R.drawable.mapaplaceholder),
            contentDescription = "Mapa interactivo",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        )
        
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

@Composable private fun MisAlquileresContent(navController: NavController) {
    val context = LocalContext.current
    var alquileresUsuario by remember { mutableStateOf(ReservationManager.getUserRentals()) }
    val todasLasBicicletas = remember { LocalJsonReader.getBicicletas(context) }
    val todosLosProveedores = remember { LocalJsonReader.getProveedores(context) }
    
    // Función para refrescar los alquileres
    fun refreshRentals() {
        alquileresUsuario = ReservationManager.getUserRentals()
    }
    
    // Alquileres predefinidos con datos más aleatorios y realistas
    val alquileresPredefinidos = remember {
        listOf(
            Alquiler(
                id = 1001,
                bicicletaId = 1,
                usuarioId = 1,
                fechaInicio = "2024-12-15",
                fechaFin = "2024-12-16",
                precioPorHora = 10.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - Monterrico",
                ubicacionDevolucion = "UPC - Monterrico",
                notas = "Alquiler completado exitosamente",
                tiempoInicioActivo = 0L,
                precioTotal = 180.0
            ),
            Alquiler(
                id = 1002,
                bicicletaId = 2,
                usuarioId = 1,
                fechaInicio = "2024-12-10",
                fechaFin = "2024-12-11",
                precioPorHora = 12.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - San Miguel",
                ubicacionDevolucion = "UPC - San Miguel",
                notas = "Excelente servicio",
                tiempoInicioActivo = 0L,
                precioTotal = 200.0
            ),
            Alquiler(
                id = 1003,
                bicicletaId = 3,
                usuarioId = 1,
                fechaInicio = "2024-12-05",
                fechaFin = "2024-12-06",
                precioPorHora = 12.5,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - Monterrico",
                ubicacionDevolucion = "UPC - Monterrico",
                notas = "Bicicleta en excelente estado",
                tiempoInicioActivo = 0L,
                precioTotal = 220.0
            ),
            Alquiler(
                id = 1004,
                bicicletaId = 1,
                usuarioId = 1,
                fechaInicio = "2024-11-28",
                fechaFin = "2024-11-29",
                precioPorHora = 10.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - Monterrico",
                ubicacionDevolucion = "UPC - Monterrico",
                notas = "Segundo alquiler",
                tiempoInicioActivo = 0L,
                precioTotal = 190.0
            ),
            Alquiler(
                id = 1005,
                bicicletaId = 2,
                usuarioId = 1,
                fechaInicio = "2024-11-20",
                fechaFin = "2024-11-21",
                precioPorHora = 12.0,
                estado = "COMPLETADO",
                ubicacionRecogida = "UPC - San Miguel",
                ubicacionDevolucion = "UPC - San Miguel",
                notas = "Primer alquiler",
                tiempoInicioActivo = 0L,
                precioTotal = 210.0
            )
        )
    }
    
    // Crear bicicletas ficticias para los alquileres predefinidos
    val bicicletasFicticias = remember {
        mapOf(
            1001 to Bicicleta(
                id = 1,
                proveedorId = 1,
                modelo = "TREK FUEL EX",
                marca = "Trek",
                tipo = "Mountain Bike",
                precioPorHora = 10.0,
                ubicacion = "UPC - Monterrico",
                disponible = true,
                rating = 4.8,
                descripcion = "Bicicleta de montaña de alta gama",
                imagen = "trek_fuel.jpg"
            ),
            1002 to Bicicleta(
                id = 2,
                proveedorId = 2,
                modelo = "GIANT TRINITY",
                marca = "Giant",
                tipo = "Road Bike",
                precioPorHora = 12.0,
                ubicacion = "UPC - San Miguel",
                disponible = true,
                rating = 4.6,
                descripcion = "Bicicleta de carretera profesional",
                imagen = "giant_trinity.jpg"
            ),
            1003 to Bicicleta(
                id = 3,
                proveedorId = 3,
                modelo = "CANNONDALE JEKILL",
                marca = "Cannondale",
                tipo = "Trail",
                precioPorHora = 12.5,
                ubicacion = "UPC - Monterrico",
                disponible = true,
                rating = 4.9,
                descripcion = "Bicicleta trail versátil",
                imagen = "cannondale_jekyll.jpg"
            ),
            1004 to Bicicleta(
                id = 4,
                proveedorId = 1,
                modelo = "SANTACRUZ HIGHTOWER",
                marca = "Santa Cruz",
                tipo = "Enduro",
                precioPorHora = 10.0,
                ubicacion = "UPC - Monterrico",
                disponible = true,
                rating = 4.7,
                descripcion = "Bicicleta enduro de alta performance",
                imagen = "santacruz_hightower.jpg"
            ),
            1005 to Bicicleta(
                id = 5,
                proveedorId = 2,
                modelo = "ORBEA OIZ",
                marca = "Orbea",
                tipo = "Cross Country",
                precioPorHora = 12.0,
                ubicacion = "UPC - San Miguel",
                disponible = true,
                rating = 4.5,
                descripcion = "Bicicleta de cross country ligera",
                imagen = "orbea_oiz.jpg"
            )
        )
    }
    
    // Combinar alquileres predefinidos con los del usuario
    val todosLosAlquileres = alquileresPredefinidos + alquileresUsuario
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Título principal
        Text(
            text = "MIS ALQUILERES",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        // Lista de alquileres
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(todosLosAlquileres) { alquiler ->
                NuevoAlquilerItem(
                    alquiler = alquiler,
                    bicicletas = todasLasBicicletas,
                    proveedores = todosLosProveedores,
                    navController = navController,
                    onRefresh = { refreshRentals() }
                )
                
                // Separador verde entre items (excepto el último)
                if (alquiler != todosLosAlquileres.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = colorResource(id = R.color.lime_green),
                        thickness = 1.dp
                    )
                }
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
                    painter = painterResource(id = R.drawable.bikelablogo),
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

// Nuevo componente de alquiler estilo imagen
@Composable
private fun NuevoAlquilerItem(
    alquiler: Alquiler,
    bicicletas: List<Bicicleta>,
    proveedores: List<Proveedor>,
    navController: NavController,
    onRefresh: () -> Unit
) {
    // Usar bicicleta ficticia para alquileres predefinidos, o buscar en la lista real
    val bicicleta = if (alquiler.id >= 1001) {
        // Alquiler predefinido - usar bicicleta ficticia
        when (alquiler.id) {
            1001 -> Bicicleta(id = 1, proveedorId = 1, modelo = "TREK FUEL EX", marca = "Trek", tipo = "Mountain Bike", precioPorHora = 10.0, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.8, descripcion = "Bicicleta de montaña de alta gama", imagen = "trek_fuel.jpg")
            1002 -> Bicicleta(id = 2, proveedorId = 2, modelo = "GIANT TRINITY", marca = "Giant", tipo = "Road Bike", precioPorHora = 12.0, ubicacion = "UPC - San Miguel", disponible = true, rating = 4.6, descripcion = "Bicicleta de carretera profesional", imagen = "giant_trinity.jpg")
            1003 -> Bicicleta(id = 3, proveedorId = 3, modelo = "CANNONDALE JEKILL", marca = "Cannondale", tipo = "Trail", precioPorHora = 12.5, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.9, descripcion = "Bicicleta trail versátil", imagen = "cannondale_jekyll.jpg")
            1004 -> Bicicleta(id = 4, proveedorId = 1, modelo = "SANTACRUZ HIGHTOWER", marca = "Santa Cruz", tipo = "Enduro", precioPorHora = 10.0, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.7, descripcion = "Bicicleta enduro de alta performance", imagen = "santacruz_hightower.jpg")
            1005 -> Bicicleta(id = 5, proveedorId = 2, modelo = "ORBEA OIZ", marca = "Orbea", tipo = "Cross Country", precioPorHora = 12.0, ubicacion = "UPC - San Miguel", disponible = true, rating = 4.5, descripcion = "Bicicleta de cross country ligera", imagen = "orbea_oiz.jpg")
            else -> bicicletas.find { it.id == alquiler.bicicletaId }
        }
    } else {
        // Alquiler del usuario - buscar en la lista real
        bicicletas.find { it.id == alquiler.bicicletaId }
    }
    
    val proveedor = bicicleta?.let { 
        proveedores.find { prov -> prov.id == it.proveedorId }
    }
    
    // Formatear fecha para mostrar
    val fechaFormateada = when (alquiler.estado) {
        "COMPLETADO" -> {
            val fecha = java.time.LocalDate.parse(alquiler.fechaInicio)
            val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
            fecha.format(formatter)
        }
        "PENDIENTE" -> "Pendiente"
        "ACTIVO" -> "Activo"
        else -> "Activo"
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { 
                // Navegar a la pantalla de detalles del alquiler
                navController.navigate("alquiler-detail/${alquiler.id}")
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar circular (usando el logo por ahora)
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.bikelablogo),
                contentDescription = "Avatar",
                modifier = Modifier.size(30.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        // Información del alquiler
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Bicicleta ${bicicleta?.modelo ?: "Desconocida"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = when (alquiler.estado) {
                    "COMPLETADO" -> "Finalizado - $fechaFormateada"
                    "PENDIENTE" -> "Pendiente"
                    "ACTIVO" -> "Activo"
                    else -> "Activo"
                },
                fontSize = 14.sp,
                color = Color.White
            )
        }
        
        // Indicador de dropdown (texto simple)
        Text(
            text = "▼",
            fontSize = 16.sp,
            color = colorResource(id = R.color.lime_green),
            modifier = Modifier.padding(8.dp)
        )
    }
}
