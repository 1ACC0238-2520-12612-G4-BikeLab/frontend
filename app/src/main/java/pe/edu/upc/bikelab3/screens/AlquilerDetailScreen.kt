package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import kotlinx.coroutines.delay
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.Alquiler
import pe.edu.upc.bikelab3.network.Bicicleta
import pe.edu.upc.bikelab3.network.LocalJsonReader
import pe.edu.upc.bikelab3.network.Proveedor
import pe.edu.upc.bikelab3.network.ReservationManager
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
private fun ServiceDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = "$label: $value",
            fontSize = 14.sp,
            color = Color.Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlquilerDetailScreen(navController: NavController, alquilerId: Int) {
    val context = LocalContext.current
    
    // Estados para interactividad
    var isLiked by remember { mutableStateOf(false) }
    var rating by remember { mutableStateOf(4) }
    
    // Generar datos aleatorios únicos para cada alquiler
    val datosAleatorios = remember(alquilerId) {
        val random = java.util.Random(alquilerId.toLong()) // Seed basado en ID para consistencia
        
        val fechasInicio = listOf("2024-12-15", "2024-12-10", "2024-12-05", "2024-11-28", "2024-11-20", "2024-11-15", "2024-11-10")
        val fechasFin = listOf("2024-12-16", "2024-12-11", "2024-12-06", "2024-11-29", "2024-11-21", "2024-11-16", "2024-11-11")
        val horasInicio = listOf("10:15 AM", "09:30 AM", "11:00 AM", "08:45 AM", "10:45 AM", "09:15 AM", "11:30 AM")
        val horasFin = listOf("12:40 PM", "11:55 AM", "13:25 PM", "10:30 AM", "12:15 PM", "11:00 AM", "13:45 PM")
        val duraciones = listOf("2h 25min", "2h 25min", "2h 25min", "1h 45min", "1h 30min", "1h 45min", "2h 15min")
        val ubicaciones = listOf("UPC - Monterrico", "UPC - San Miguel", "UPC - Villa", "UPC - Surco", "UPC - Miraflores")
        val categorias = listOf("12 $$ • 1.2 miles away", "15 $$ • 0.8 miles away", "10 $$ • 2.1 miles away", "18 $$ • 1.5 miles away", "14 $$ • 0.9 miles away")
        val estadosIniciales = listOf("Sin observaciones", "Excelente estado", "Perfecto", "Sin daños", "Muy bueno")
        val estadosFinales = listOf("Sin observaciones", "Excelente estado", "Perfecto", "Sin daños", "Muy bueno")
        
        mapOf(
            "fechaInicio" to fechasInicio[random.nextInt(fechasInicio.size)],
            "fechaFin" to fechasFin[random.nextInt(fechasFin.size)],
            "horaInicio" to horasInicio[random.nextInt(horasInicio.size)],
            "horaFin" to horasFin[random.nextInt(horasFin.size)],
            "duracion" to duraciones[random.nextInt(duraciones.size)],
            "ubicacionRecogida" to ubicaciones[random.nextInt(ubicaciones.size)],
            "ubicacionDevolucion" to ubicaciones[random.nextInt(ubicaciones.size)],
            "categoria" to categorias[random.nextInt(categorias.size)],
            "estadoInicial" to estadosIniciales[random.nextInt(estadosIniciales.size)],
            "estadoFinal" to estadosFinales[random.nextInt(estadosFinales.size)]
        )
    }
    
    // Buscar el alquiler
    val todosLosAlquileres = remember {
        // Alquileres predefinidos + alquileres del usuario
        val alquileresPredefinidos = listOf(
            Alquiler(id = 1001, bicicletaId = 1, usuarioId = 1, fechaInicio = "2024-12-15", fechaFin = "2024-12-16", precioPorHora = 10.0, estado = "COMPLETADO", ubicacionRecogida = "UPC - Monterrico", ubicacionDevolucion = "UPC - Monterrico", notas = "Alquiler completado exitosamente", tiempoInicioActivo = 0L, precioTotal = 180.0),
            Alquiler(id = 1002, bicicletaId = 2, usuarioId = 1, fechaInicio = "2024-12-10", fechaFin = "2024-12-11", precioPorHora = 12.0, estado = "COMPLETADO", ubicacionRecogida = "UPC - San Miguel", ubicacionDevolucion = "UPC - San Miguel", notas = "Excelente servicio", tiempoInicioActivo = 0L, precioTotal = 200.0),
            Alquiler(id = 1003, bicicletaId = 3, usuarioId = 1, fechaInicio = "2024-12-05", fechaFin = "2024-12-06", precioPorHora = 12.5, estado = "COMPLETADO", ubicacionRecogida = "UPC - Monterrico", ubicacionDevolucion = "UPC - Monterrico", notas = "Bicicleta en excelente estado", tiempoInicioActivo = 0L, precioTotal = 220.0),
            Alquiler(id = 1004, bicicletaId = 1, usuarioId = 1, fechaInicio = "2024-11-28", fechaFin = "2024-11-29", precioPorHora = 10.0, estado = "COMPLETADO", ubicacionRecogida = "UPC - Monterrico", ubicacionDevolucion = "UPC - Monterrico", notas = "Segundo alquiler", tiempoInicioActivo = 0L, precioTotal = 190.0),
            Alquiler(id = 1005, bicicletaId = 2, usuarioId = 1, fechaInicio = "2024-11-20", fechaFin = "2024-11-21", precioPorHora = 12.0, estado = "COMPLETADO", ubicacionRecogida = "UPC - San Miguel", ubicacionDevolucion = "UPC - San Miguel", notas = "Primer alquiler", tiempoInicioActivo = 0L, precioTotal = 210.0)
        )
        alquileresPredefinidos + ReservationManager.getUserRentals()
    }
    
    val alquiler = todosLosAlquileres.find { it.id == alquilerId }
    
    if (alquiler == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Alquiler no encontrado")
        }
        return
    }
    
    // Obtener bicicleta y proveedor
    val bicicleta = if (alquiler.id >= 1001) {
        // Bicicleta ficticia para alquileres predefinidos
        when (alquiler.id) {
            1001 -> Bicicleta(id = 1, proveedorId = 1, modelo = "TREK FUEL EX", marca = "Trek", tipo = "Mountain Bike", precioPorHora = 10.0, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.8, descripcion = "Bicicleta de montaña de alta gama", imagen = "trek_fuel.jpg")
            1002 -> Bicicleta(id = 2, proveedorId = 2, modelo = "GIANT TRINITY", marca = "Giant", tipo = "Road Bike", precioPorHora = 12.0, ubicacion = "UPC - San Miguel", disponible = true, rating = 4.6, descripcion = "Bicicleta de carretera profesional", imagen = "giant_trinity.jpg")
            1003 -> Bicicleta(id = 3, proveedorId = 3, modelo = "CANNONDALE JEKILL", marca = "Cannondale", tipo = "Trail", precioPorHora = 12.5, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.9, descripcion = "Bicicleta trail versátil", imagen = "cannondale_jekyll.jpg")
            1004 -> Bicicleta(id = 4, proveedorId = 1, modelo = "SANTACRUZ HIGHTOWER", marca = "Santa Cruz", tipo = "Enduro", precioPorHora = 10.0, ubicacion = "UPC - Monterrico", disponible = true, rating = 4.7, descripcion = "Bicicleta enduro de alta performance", imagen = "santacruz_hightower.jpg")
            1005 -> Bicicleta(id = 5, proveedorId = 2, modelo = "ORBEA OIZ", marca = "Orbea", tipo = "Cross Country", precioPorHora = 12.0, ubicacion = "UPC - San Miguel", disponible = true, rating = 4.5, descripcion = "Bicicleta de cross country ligera", imagen = "orbea_oiz.jpg")
            else -> null
        }
    } else {
        // Buscar en db.json o crear bicicleta por defecto
        LocalJsonReader.getBicicletas(context).find { it.id == alquiler.bicicletaId }
            ?: Bicicleta(
                id = alquiler.bicicletaId,
                proveedorId = 1,
                modelo = "BICICLETA GENÉRICA",
                marca = "BikeLab",
                tipo = "Mountain Bike",
                precioPorHora = 10.0,
                ubicacion = "UPC - Monterrico",
                disponible = true,
                rating = 4.5,
                descripcion = "Bicicleta disponible para alquiler",
                imagen = "placeholdertecla2"
            )
    }
    
    val proveedor = bicicleta?.let { 
        LocalJsonReader.getProveedores(context).find { prov -> prov.id == it.proveedorId }
            ?: Proveedor(
                id = it.proveedorId,
                nombre = "Proveedor",
                apellido = "BikeLab",
                correo = "proveedor@bikelab.com",
                telefono = "999999999",
                direccion = it.ubicacion,
                rating = 4.5,
                bicicletasRegistradas = 1,
                fechaRegistro = "2024-01-01"
            )
    }
    
    // Estado para el contador en tiempo real (solo para alquileres activos)
    var tiempoTranscurrido by remember { mutableStateOf(0L) }
    var precioActual by remember { mutableStateOf(0.0) }
    
    // Timer para alquileres activos
    LaunchedEffect(alquiler.estado, alquiler.tiempoInicioActivo) {
        if (alquiler.estado == "ACTIVO" && alquiler.tiempoInicioActivo > 0) {
            while (true) {
                tiempoTranscurrido = System.currentTimeMillis() - alquiler.tiempoInicioActivo
                val horasUsadas = tiempoTranscurrido / (1000 * 60 * 60.0)
                precioActual = alquiler.precioPorHora * horasUsadas
                kotlinx.coroutines.delay(1000) // Actualizar cada segundo
            }
        }
    }
    
    // Formatear tiempo transcurrido
    val horas = (tiempoTranscurrido / (1000 * 60 * 60)).toInt()
    val minutos = ((tiempoTranscurrido % (1000 * 60 * 60)) / (1000 * 60)).toInt()
    val segundos = ((tiempoTranscurrido % (1000 * 60)) / 1000).toInt()
    val tiempoFormateado = String.format("%02d:%02d:%02d", horas, minutos, segundos)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Fondo negro para que no se vea blanco
    ) {
        // Imagen de fondo que cubre toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.placeholdertecla2),
            contentDescription = "Bicicleta ${bicicleta?.modelo}",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Sección superior con imagen, texto y botones (estática) - desde arriba hasta la parte blanca
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            ) {
                // Botón de regreso
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = Color.Black
                    )
                }
                
                // Información sobre la imagen y botones
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Bicicleta ${bicicleta?.modelo ?: "Desconocida"}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    Text(
                        text = "Arrendado por ${proveedor?.nombre ?: "Proveedor"} ${proveedor?.apellido ?: ""}",
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.lime_green)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Botones de fecha y hora integrados en la imagen
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.lime_green)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Fecha",
                                    tint = Color.White
                                )
                                Text(
                                    text = when (alquiler.estado) {
                                        "PENDIENTE" -> LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        "ACTIVO" -> {
                                            val fechaInicio = java.time.Instant.ofEpochMilli(alquiler.tiempoInicioActivo)
                                                .atZone(java.time.ZoneId.systemDefault())
                                                .toLocalDate()
                                            fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        }
                                        else -> {
                                            val fecha = LocalDate.parse(datosAleatorios["fechaInicio"]!!)
                                            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                        }
                                    },
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                        
                        Button(
                            onClick = { },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.lime_green)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Hora",
                                    tint = Color.White
                                )
                                Text(
                                    text = when (alquiler.estado) {
                                        "PENDIENTE" -> "${datosAleatorios["horaInicio"]} - ${datosAleatorios["horaFin"]}"
                                        "ACTIVO" -> "${datosAleatorios["horaInicio"]} - En curso"
                                        else -> "${datosAleatorios["horaInicio"]} - ${datosAleatorios["horaFin"]}"
                                    },
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
            
            // Contenido principal scrolleable (parte blanca)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White) // Fondo blanco para el contenido
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Sección "Sobre el Servicio"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sobre el Servicio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Rating con estrellas clickeables
                        Row {
                            repeat(5) { index ->
                                Text(
                                    text = if (index < rating) "★" else "☆",
                                    fontSize = 16.sp,
                                    color = if (index < rating) colorResource(id = R.color.lime_green) else Color.Gray,
                                    modifier = Modifier.clickable { 
                                        rating = index + 1
                                    }
                                )
                            }
                        }
                        
                        Text(
                            text = if (isLiked) "♥" else "♡",
                            fontSize = 16.sp,
                            color = if (isLiked) Color.Red else Color.Gray,
                            modifier = Modifier.clickable { 
                                isLiked = !isLiked
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Detalles del servicio
                ServiceDetailItem("Fecha de inicio", when (alquiler.estado) {
                    "PENDIENTE" -> "${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} – ${datosAleatorios["horaInicio"]}"
                    "ACTIVO" -> {
                        val fechaInicio = java.time.Instant.ofEpochMilli(alquiler.tiempoInicioActivo)
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate()
                        "${fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} – ${datosAleatorios["horaInicio"]}"
                    }
                    else -> "${LocalDate.parse(datosAleatorios["fechaInicio"]!!).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} – ${datosAleatorios["horaInicio"]}"
                })
                
                ServiceDetailItem("Fecha de finalización", when (alquiler.estado) {
                    "PENDIENTE" -> "${LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} – ${datosAleatorios["horaFin"]}"
                    "ACTIVO" -> "En curso"
                    else -> "${LocalDate.parse(datosAleatorios["fechaFin"]!!).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))} – ${datosAleatorios["horaFin"]}"
                })
                
                ServiceDetailItem("Duración total", when (alquiler.estado) {
                    "PENDIENTE" -> datosAleatorios["duracion"]!!
                    "ACTIVO" -> tiempoFormateado
                    else -> datosAleatorios["duracion"]!!
                })
                
                ServiceDetailItem("Plan usado", "Pago por hora")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Sección "Detalles de la bicicleta"
                Text(
                    text = "Detalles de la bicicleta",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ServiceDetailItem("Category", datosAleatorios["categoria"]!!)
                ServiceDetailItem("ID Bicicleta", "BK-${String.format("%04d", alquiler.bicicletaId)}")
                ServiceDetailItem("Estado inicial", datosAleatorios["estadoInicial"]!!)
                ServiceDetailItem("Estado final", datosAleatorios["estadoFinal"]!!)
                ServiceDetailItem("Punto de recogida", datosAleatorios["ubicacionRecogida"]!!)
                ServiceDetailItem("Punto de devolución", datosAleatorios["ubicacionDevolucion"]!!)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Sección final según el estado
                when (alquiler.estado) {
                    "PENDIENTE" -> {
                        // Botón ACTIVAR para alquileres pendientes
                        Button(
                            onClick = { 
                                ReservationManager.activateRental(alquiler.id)
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(id = R.color.lime_green)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "ACTIVAR ALQUILER",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    "ACTIVO" -> {
                        // Información de tiempo actual y botón finalizar
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.lime_green).copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "⏱️ Tiempo de uso actual",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                
                                Spacer(modifier = Modifier.height(8.dp))
                                
                                Text(
                                    text = tiempoFormateado,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colorResource(id = R.color.lime_green)
                                )
                                
                                Text(
                                    text = "Total actual: S/ ${String.format("%.2f", precioActual)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Button(
                                    onClick = { 
                                        ReservationManager.finalizeRental(alquiler.id)
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "FINALIZAR ALQUILER",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                    else -> {
                        // Pregunta de feedback para alquileres completados
                        Text(
                            text = "¿Tuvo algún problema con el servicio brindado?",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6A1B9A) // Púrpura oscuro
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "✓ Sí",
                                    fontSize = 14.sp,
                                    color = Color.White
                                )
                            }
                            
                            Button(
                                onClick = { navController.popBackStack() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.LightGray
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "NO",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}