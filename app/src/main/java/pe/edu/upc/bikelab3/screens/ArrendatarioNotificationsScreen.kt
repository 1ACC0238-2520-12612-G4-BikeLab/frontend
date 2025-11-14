package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.upc.bikelab3.R
import pe.edu.upc.bikelab3.network.NotificationManager
import pe.edu.upc.bikelab3.network.UserSession

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArrendatarioNotificationsScreen(navController: NavController) {
    val notifications = remember { mutableStateListOf(*NotificationManager.getAllNotifications().toTypedArray()) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Manejar el botón de retroceso cuando el drawer está abierto
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch {
            drawerState.close()
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
                    modifier = Modifier.clickable { 
                        navController.navigate("arrendatario-home")
                        scope.launch { drawerState.close() } 
                    },
                    headlineContent = {
                        Text(
                            text = "Inicio",
                            color = Color.Black
                        )
                    }
                )
                
                ListItem(
                    modifier = Modifier.clickable { 
                        navController.navigate("arrendatario-home")
                        scope.launch { drawerState.close() } 
                    },
                    headlineContent = {
                        Text(
                            text = "Agregar Vehículos",
                            color = Color.Black
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
                            text = "Notificaciones",
                            color = colorResource(id = R.color.lime_green),
                            fontWeight = FontWeight.Bold
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "NOTIFICACIONES",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Filtro "Order by"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order by",
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = "Más recientes",
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.lime_green),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de notificaciones
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notifications) { notification ->
                    ArrendatarioNotificationCard(notification)
                }
            }
        }
    }
    }
}

@Composable
fun ArrendatarioNotificationCard(notification: pe.edu.upc.bikelab3.network.Notificacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Black),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar/Icono
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorResource(id = R.color.lime_green)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Notificación",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Contenido de la notificación
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.remitente,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = notification.mensaje,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = notification.timestamp,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Botones de acción
            Row {
                IconButton(
                    onClick = { /* Eliminar notificación */ }
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(
                    onClick = { /* Archivar notificación */ }
                ) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = "Más opciones",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
