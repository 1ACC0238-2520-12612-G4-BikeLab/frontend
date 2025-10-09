package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.UserSession
import pe.edu.upc.bikelab3.network.VehiculoManager
import pe.edu.upc.bikelab3.network.VehiculoRegistrado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarVehiculoScreen(navController: NavController, vehiculoId: Int) {
    val vehiculoOriginal = remember { 
        VehiculoManager.getVehiculoPorId(vehiculoId) 
    }
    
    var nombre by remember { mutableStateOf(vehiculoOriginal?.marcaModelo ?: "") }
    var tipo by remember { mutableStateOf(vehiculoOriginal?.tipo ?: "Bicicleta") }
    var precioHora by remember { mutableStateOf(vehiculoOriginal?.precioPorHora ?: "0.0") }
    var precioDia by remember { mutableStateOf(vehiculoOriginal?.precioPorDia ?: "0.0") }
    var ubicacion by remember { mutableStateOf(vehiculoOriginal?.ubicacionActual ?: "") }
    var disponible by remember { mutableStateOf(vehiculoOriginal?.disponible ?: true) }
    
    val tiposVehiculo = listOf("Bicicleta", "Scooter")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar Vehículo",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                text = "Modificar Datos del Vehículo",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Comparación de datos antiguos vs nuevos
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Datos Actuales vs Nuevos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    // Nombre
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Nombre:", color = Color.Gray, fontSize = 14.sp)
                        Text("${vehiculoOriginal?.marcaModelo} → $nombre", color = Color.White, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Tipo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Tipo:", color = Color.Gray, fontSize = 14.sp)
                        Text("${vehiculoOriginal?.tipo} → $tipo", color = Color.White, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Precio por hora
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Precio/hora:", color = Color.Gray, fontSize = 14.sp)
                        Text("S/ ${vehiculoOriginal?.precioPorHora} → S/ $precioHora", color = Color.White, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Precio por día
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Precio/día:", color = Color.Gray, fontSize = 14.sp)
                        Text("S/ ${vehiculoOriginal?.precioPorDia} → S/ $precioDia", color = Color.White, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Ubicación
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Ubicación:", color = Color.Gray, fontSize = 14.sp)
                        Text("${vehiculoOriginal?.ubicacionActual} → $ubicacion", color = Color.White, fontSize = 14.sp)
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Disponibilidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Disponibilidad:", color = Color.Gray, fontSize = 14.sp)
                        Text("${if (vehiculoOriginal?.disponible == true) "Disponible" else "No Disponible"} → ${if (disponible) "Disponible" else "No Disponible"}", color = Color.White, fontSize = 14.sp)
                    }
                }
            }
            
            // Formulario de edición
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Formulario de Edición",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    // Nombre
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre del Vehículo", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    
                    // Tipo de vehículo
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = tipo,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Vehículo", color = Color.Gray) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = colorResource(id = R.color.lime_green),
                                unfocusedBorderColor = Color.Gray
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            tiposVehiculo.forEach { tipoSeleccionado ->
                                DropdownMenuItem(
                                    text = { Text(tipoSeleccionado, color = Color.White) },
                                    onClick = {
                                        tipo = tipoSeleccionado
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    // Precio por hora
                    OutlinedTextField(
                        value = precioHora,
                        onValueChange = { precioHora = it },
                        label = { Text("Precio por Hora (S/)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    
                    // Precio por día
                    OutlinedTextField(
                        value = precioDia,
                        onValueChange = { precioDia = it },
                        label = { Text("Precio por Día (S/)", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    
                    // Ubicación
                    OutlinedTextField(
                        value = ubicacion,
                        onValueChange = { ubicacion = it },
                        label = { Text("Ubicación Actual", color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = colorResource(id = R.color.lime_green),
                            unfocusedBorderColor = Color.Gray
                        )
                    )
                    
                    // Disponibilidad
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Disponibilidad:",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Switch(
                            checked = disponible,
                            onCheckedChange = { disponible = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = colorResource(id = R.color.lime_green),
                                checkedTrackColor = colorResource(id = R.color.lime_green).copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            }
            
            // Botón Guardar
            Button(
                onClick = {
                    val vehiculoActualizado = vehiculoOriginal?.copy(
                        marcaModelo = nombre,
                        tipo = tipo,
                        precioPorHora = precioHora,
                        precioPorDia = precioDia,
                        ubicacionActual = ubicacion,
                        disponible = disponible
                    )
                    if (vehiculoActualizado != null) {
                        VehiculoManager.actualizarVehiculo(vehiculoActualizado)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.lime_green)
                )
            ) {
                Text(
                    text = "Guardar Cambios",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
