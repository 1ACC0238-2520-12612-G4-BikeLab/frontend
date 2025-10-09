package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.Alquiler
import pe.edu.upc.bikelab3.network.Bicicleta
import pe.edu.upc.bikelab3.network.LocalJsonReader
import pe.edu.upc.bikelab3.network.NotificationManager
import pe.edu.upc.bikelab3.network.Proveedor
import pe.edu.upc.bikelab3.network.ReservationManager
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RentScreen(navController: NavController, bicicletaId: Int) {
    val context = LocalContext.current
    
    // Buscar primero en db.json
    val bicicletaConProveedor = remember { 
        LocalJsonReader.getBicicletaConProveedor(context, bicicletaId) 
    }
    val bicicletaDB = bicicletaConProveedor.first
    val proveedorDB = bicicletaConProveedor.second
    
    // Si no se encuentra en db.json, buscar en vehículos registrados
    val vehiculoRegistrado = remember {
        VehiculoManager.getAllVehiculos().find { it.id == bicicletaId }
    }
    
    // Determinar qué datos usar
    val bicicleta = bicicletaDB ?: vehiculoRegistrado?.let { vehiculo ->
        Bicicleta(
            id = vehiculo.id,
            proveedorId = vehiculo.propietarioId,
            modelo = vehiculo.marcaModelo,
            marca = vehiculo.marcaModelo.split(" ").firstOrNull() ?: "Personalizada",
            tipo = vehiculo.tipo,
            precioPorHora = vehiculo.precioPorHora.toDoubleOrNull() ?: 0.0,
            ubicacion = vehiculo.ubicacionActual,
            disponible = true,
            rating = 4.5,
            descripcion = "Vehículo registrado por arrendatario",
            imagen = "placeholder"
        )
    }
    
    val proveedor = proveedorDB ?: vehiculoRegistrado?.let { vehiculo ->
        // Buscar el usuario arrendatario real en db.json
        val usuarios = LocalJsonReader.getUsuarios(context)
        val usuarioArrendatario = usuarios.find { it.id == vehiculo.propietarioId && it.tipo == "Arrendatario" }
        
        // Crear un proveedor con datos reales del arrendatario
        Proveedor(
            id = vehiculo.propietarioId,
            nombre = usuarioArrendatario?.nombre ?: "Arrendatario",
            apellido = usuarioArrendatario?.apellido ?: "Local",
            correo = usuarioArrendatario?.correo ?: "arrendatario@bikelab.pe",
            telefono = usuarioArrendatario?.numero ?: "+51 999 999 999",
            direccion = vehiculo.ubicacionActual,
            rating = 4.5,
            bicicletasRegistradas = 1,
            fechaRegistro = vehiculo.fechaRegistro,
            imagenPerfil = "default_profile.jpg"
        )
    }
    
    var showConfirmationDialog by remember { mutableStateOf(false) }

    if (bicicleta == null || proveedor == null) {
        // Manejar caso de error
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Vehículo no encontrado", color = Color.White)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Barra de navegación superior
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
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* Perfil */ }) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Black
            )
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Sección del perfil del proveedor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                // Imagen de perfil del proveedor
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bikelablogo),
                        contentDescription = "Profile",
                        modifier = Modifier.size(40.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "${proveedor.nombre.uppercase()} ${proveedor.apellido.uppercase()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.lime_green)
                    )
                    
                    Text(
                        text = "PROVEEDOR",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    // Rating con estrellas
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(4) {
                            Text(
                                text = "★",
                                fontSize = 16.sp,
                                color = colorResource(id = R.color.lime_green)
                            )
                        }
                        Text(
                            text = "☆",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
            
            // Imagen de la bicicleta
            Image(
                painter = painterResource(id = R.drawable.teclaproveedores),
                contentDescription = "Imagen de la bicicleta ${bicicleta.modelo}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Panel de detalles de la bicicleta
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    // Detalles de la bicicleta
                    DetailRow("MODELO", bicicleta.modelo)
                    DetailRow("TIEMPO DE USO", if (vehiculoRegistrado != null) "Recién añadido" else "2 años")
                    DetailRow("ACEPTO", "Yape, Plin")
                    
                    // Línea separadora
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.White
                    )
                    
                    // Total
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        
                        Text(
                            text = "S/ ${String.format("%.0f", bicicleta.precioPorHora)} X HORA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.lime_green)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botón RESERVAR
                Button(
                    onClick = { showConfirmationDialog = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.lime_green)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "RESERVAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                
                // Botón CANCELAR
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.lime_green)
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        2.dp, 
                        colorResource(id = R.color.lime_green)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "CANCELAR",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        
        // Diálogo de confirmación
        if (showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmationDialog = false },
                title = {
                    Text(
                        text = "Mensaje de confirmación",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                text = {
                    Text(
                        text = "Su reserva fue exitosa. Gracias por usar BikeLab",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = { 
                            showConfirmationDialog = false
                            ReservationManager.reserveBike(bicicleta.id)
                            
                            // Crear alquiler
                            val currentUser = UserSession.currentUser
                            if (currentUser != null) {
                                val nuevoAlquiler = Alquiler(
                                    id = ReservationManager.getUserRentals().size + 1,
                                    bicicletaId = bicicleta.id,
                                    usuarioId = currentUser.id,
                                    fechaInicio = "2024-12-19",
                                    fechaFin = "2024-12-20",
                                    precioPorHora = bicicleta.precioPorHora,
                                    estado = "PENDIENTE",
                                    ubicacionRecogida = bicicleta.ubicacion,
                                    ubicacionDevolucion = bicicleta.ubicacion,
                                    notas = "Alquiler de ${bicicleta.modelo}",
                                    tiempoInicioActivo = 0L,
                                    precioTotal = 0.0
                                )
                                ReservationManager.addRental(nuevoAlquiler)
                                
                                // Crear notificación de reserva
                                val proveedor = proveedor
                                val notificacionReserva = NotificationManager.createReservationNotification(
                                    bicicleta.modelo,
                                    "${proveedor?.nombre} ${proveedor?.apellido}"
                                )
                                NotificationManager.addNotification(notificacionReserva)
                            }
                            
                            navController.popBackStack()
                        }
                    ) {
                        Text(
                            text = "Cerrar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            fontSize = 14.sp,
            color = Color.White
        )
        
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}
