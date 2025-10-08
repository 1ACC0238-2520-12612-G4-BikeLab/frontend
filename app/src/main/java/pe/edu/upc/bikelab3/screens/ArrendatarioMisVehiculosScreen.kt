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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager

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
                0 -> MisVehiculosContent()
                1 -> AlquileresActivosContent()
                2 -> HistorialContent()
            }
        }
    }
}

@Composable
fun MisVehiculosContent() {
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

        if (vehiculosRegistrados.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "No hay vehículos registrados",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Ve a 'Agregar Vehículo' para registrar tu primer vehículo",
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
                items(vehiculosRegistrados) { vehiculo ->
                    VehiculoRegistradoCard(vehiculo) {
                        vehiculosRegistrados.remove(vehiculo)
                        VehiculoManager.eliminarVehiculo(vehiculo.id)
                    }
                }
            }
        }
    }
}

@Composable
fun AlquileresActivosContent() {
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

        // Lista de alquileres activos (por ahora vacía)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
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
            }
        }
    }
}

@Composable
fun HistorialContent() {
    val historialVehiculos = listOf(
        HistorialVehiculo(
            id = 1,
            modelo = "SPECIALIZED ENDURO",
            marca = "Specialized",
            tipo = "Mountain Bike",
            fechaAlquiler = "2024-01-15",
            duracion = "3 horas",
            precioTotal = 30.00,
            cliente = "Violet Norman",
            estado = "Completado"
        ),
        HistorialVehiculo(
            id = 2,
            modelo = "CANNONDALE SCALPEL",
            marca = "Cannondale",
            tipo = "Cross Country",
            fechaAlquiler = "2024-01-20",
            duracion = "2 horas",
            precioTotal = 24.00,
            cliente = "Sebastian Hernandez",
            estado = "Completado"
        ),
        HistorialVehiculo(
            id = 3,
            modelo = "SCOTT SPARK",
            marca = "Scott",
            tipo = "Trail",
            fechaAlquiler = "2024-02-05",
            duracion = "1 día",
            precioTotal = 50.00,
            cliente = "Ana García",
            estado = "Completado"
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
                HistorialVehiculoCard(vehiculo)
            }
        }
    }
}

@Composable
fun HistorialVehiculoCard(vehiculo: HistorialVehiculo) {
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
                }
                
                Text(
                    text = "S/ ${String.format("%.2f", vehiculo.precioTotal)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.lime_green)
                )
            }
        }
    }
}

@Composable
fun VehiculoRegistradoCard(vehiculo: pe.edu.upc.bikelab3.network.VehiculoRegistrado, onDelete: () -> Unit) {
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
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
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
        }
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
    val estado: String
)
