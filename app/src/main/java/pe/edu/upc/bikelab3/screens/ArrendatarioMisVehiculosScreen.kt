package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager
import pe.edu.upc.bikelab3.network.AlquilerActivoJsonReader
import pe.edu.upc.bikelab3.network.AlquilerActivo
import pe.edu.upc.bikelab3.network.VehiculoJsonReader
import pe.edu.upc.bikelab3.network.LocalJsonReader
import pe.edu.upc.bikelab3.network.ReseñaJsonReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrendatarioMisVehiculosScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mis Vehículos", "Alquileres Activos", "Historial")

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
                    IconButton(onClick = { navController.navigate("arrendatario-home") }) {
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
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Black,
                contentColor = colorResource(id = R.color.lime_green)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) 
                                    colorResource(id = R.color.lime_green) 
                                else 
                                    Color.Gray
                            )
                        }
                    )
                }
            }

            // Contenido según la pestaña seleccionada
            when (selectedTab) {
                0 -> MisVehiculosContent(navController)
                1 -> AlquileresActivosContent()
                2 -> HistorialContent(navController)
            }
        }
    }
}

@Composable
fun MisVehiculosContent(navController: NavController) {
    val vehiculosRegistrados = remember { 
        mutableStateListOf(*VehiculoManager.getVehiculosPorPropietario(UserSession.currentUser?.id ?: 0).toTypedArray())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Vehículos Registrados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(vehiculosRegistrados) { vehiculo ->
                VehiculoRegistradoCard(
                    vehiculo = vehiculo,
                    onEdit = { 
                        // Navegar al formulario de edición
                        navController.navigate("editar-vehiculo/${vehiculo.id}")
                    },
                    onDelete = {
                        vehiculosRegistrados.remove(vehiculo)
                        VehiculoManager.eliminarVehiculo(vehiculo.id)
                    },
                    onToggleDisponibilidad = {
                        val index = vehiculosRegistrados.indexOf(vehiculo)
                        val vehiculoActualizado = vehiculo.copy(disponible = !vehiculo.disponible)
                        vehiculosRegistrados[index] = vehiculoActualizado
                        VehiculoManager.actualizarVehiculo(vehiculoActualizado)
                    }
                )
            }
        }
    }
}

@Composable
fun AlquileresActivosContent() {
    val alquileresActivos = remember { 
        AlquilerActivoJsonReader.getAlquileresActivosPorPropietario(UserSession.currentUser?.id ?: 0)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Alquileres Activos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (alquileresActivos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "No hay alquileres activos",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Los alquileres aparecerán aquí cuando alguien reserve tus vehículos",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(alquileresActivos) { alquiler ->
                    AlquilerActivoCard(alquiler)
                }
            }
        }
    }
}

@Composable
fun HistorialContent(navController: NavController) {
    val historialVehiculos = listOf(
        HistorialVehiculo(
            id = 1001, // Cambiado para coincidir con vehiculosRegistrados
            modelo = "SPECIALIZED ENDURO",
            marca = "Specialized",
            tipo = "Mountain Bike",
            fechaAlquiler = "2024-01-15",
            duracion = "3 horas",
            precioTotal = 30.00,
            cliente = "Violet Norman",
            estado = "Completado",
            rating = 4.5
        ),
        HistorialVehiculo(
            id = 1002, // Cambiado para coincidir con vehiculosRegistrados
            modelo = "CANNONDALE SCALPEL",
            marca = "Cannondale",
            tipo = "Cross Country",
            fechaAlquiler = "2024-01-20",
            duracion = "2 horas",
            precioTotal = 24.00,
            cliente = "Sebastian Hernandez",
            estado = "Completado",
            rating = 4.6
        ),
        HistorialVehiculo(
            id = 1003, // Cambiado para coincidir con vehiculosRegistrados
            modelo = "SCOTT SPARK",
            marca = "Scott",
            tipo = "Trail",
            fechaAlquiler = "2024-02-05",
            duracion = "1 día",
            precioTotal = 50.00,
            cliente = "Ana García",
            estado = "Completado",
            rating = 4.8
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Historial de Alquileres",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historialVehiculos) { vehiculo ->
                HistorialVehiculoCard(vehiculo, navController)
            }
        }
    }
}

@Composable
fun HistorialVehiculoCard(vehiculo: HistorialVehiculo, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
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
                    text = "${vehiculo.marca} ${vehiculo.modelo}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = vehiculo.estado,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.lime_green),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = vehiculo.tipo,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Cliente: ${vehiculo.cliente}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Fecha: ${vehiculo.fechaAlquiler}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Duración: ${vehiculo.duracion}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Rating: ${String.format("%.1f", vehiculo.rating)} ⭐",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.lime_green),
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Text(
                    text = "S/ ${String.format("%.2f", vehiculo.precioTotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lime_green)
                )
            }
            
            // Botón para ver reseñas específicas del vehículo
            Button(
                onClick = { navController.navigate("reseñas-vehiculo/${vehiculo.id}") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lime_green)
                )
            ) {
                Text(
                    text = "Ver Reseñas",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VehiculoRegistradoCard(
    vehiculo: pe.edu.upc.bikelab3.network.VehiculoRegistrado, 
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleDisponibilidad: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
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
                    text = vehiculo.marcaModelo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = colorResource(id = R.color.lime_green),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Text(
                text = vehiculo.tipo,
                fontSize = 14.sp,
                color = colorResource(id = R.color.lime_green),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Precio/hora: S/ ${vehiculo.precioPorHora}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Precio/día: S/ ${vehiculo.precioPorDia}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Ubicación: ${vehiculo.ubicacionActual}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Text(
                    text = vehiculo.fechaRegistro,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            // Botón de disponibilidad
            Button(
                onClick = onToggleDisponibilidad,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (vehiculo.disponible) 
                        colorResource(id = R.color.lime_green) 
                    else 
                        Color.Red
                )
            ) {
                Text(
                    text = if (vehiculo.disponible) "Disponible" else "No Disponible",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AlquilerActivoCard(alquiler: AlquilerActivo) {
    val context = LocalContext.current
    val vehiculo = VehiculoJsonReader.getVehiculoPorId(alquiler.vehiculoId)
    val usuario = LocalJsonReader.getUsuarioPorId(context, alquiler.usuarioId)
    
    var mostrarInfoUsuario by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp)
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
                    text = vehiculo?.marcaModelo ?: "Vehículo no encontrado",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = alquiler.estado,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.lime_green),
                    fontWeight = FontWeight.Bold
                )
            }
            
            Text(
                text = vehiculo?.tipo ?: "",
                fontSize = 14.sp,
                color = colorResource(id = R.color.lime_green),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Usuario: ${usuario?.nombre ?: "Usuario"} ${usuario?.apellido ?: ""}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Fecha: ${alquiler.fechaInicio}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Precio/hora: S/ ${alquiler.precioPorHora}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                Text(
                    text = "S/ ${alquiler.precioTotal}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lime_green)
                )
            }
            
            // Botón Verificar
            Button(
                onClick = { mostrarInfoUsuario = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lime_green)
                )
            ) {
                Text(
                    text = "Verificar Usuario",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    
    // Dialog para mostrar información del usuario
    if (mostrarInfoUsuario) {
        AlertDialog(
            onDismissRequest = { mostrarInfoUsuario = false },
            title = {
                Text(
                    text = "Información del Usuario",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "Nombre: ${usuario?.nombre ?: "N/A"} ${usuario?.apellido ?: ""}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Correo: ${usuario?.correo ?: "N/A"}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Teléfono: ${usuario?.numero ?: "N/A"}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Universidad: ${usuario?.universidad ?: "N/A"}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Código: ${usuario?.codigo ?: "N/A"}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Dirección: ${usuario?.direccion ?: "N/A"}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "Viajes realizados: ${usuario?.viajes ?: 0}",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarInfoUsuario = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.lime_green)
                    )
                ) {
                    Text("Cerrar", color = Color.White)
                }
            },
            containerColor = Color.DarkGray
        )
    }
}

data class HistorialVehiculo(
    val id: Int,
    val modelo: String,
    val marca: String,
    val tipo: String,
    val fechaAlquiler: String,
    val duracion: String,
    val precioTotal: Double,
    val cliente: String,
    val estado: String,
    val rating: Double
)
