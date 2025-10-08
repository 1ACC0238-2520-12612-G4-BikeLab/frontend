package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.upc.bikelab3.network.UserSession

enum class Section { INICIO, UBICACION, NOTIFICACIONES, CONFIGURACION, PERFIL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
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
                Spacer(Modifier.height(16.dp))
                Text("Menú principal", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

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
                    label = "Configuración",
                    selected = section == Section.CONFIGURACION,
                    onClick = {
                        section = Section.CONFIGURACION
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
                DrawerItem(
                    label = "Perfil",
                    selected = section == Section.PERFIL,
                    onClick = {
                        section = Section.PERFIL
                        scope.launch { drawerState.close() }
                    },
                    icon = { Icon(Icons.Default.Person, null) }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                DrawerItem(
                    label = "Cerrar sesión",
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
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            when (section) {
                                Section.INICIO -> "Inicio"
                                Section.UBICACION -> "Ubicación"
                                Section.NOTIFICACIONES -> "Notificaciones"
                                Section.CONFIGURACION -> "Configuración"
                                Section.PERFIL -> "Perfil"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open() else drawerState.close()
                            }
                        }) { Icon(Icons.Default.Menu, contentDescription = "Menú") }
                    }
                )
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (section) {
                    Section.INICIO -> HomeContent()
                    Section.UBICACION -> LocationContent()
                    Section.NOTIFICACIONES -> NotificationsContent()
                    Section.CONFIGURACION -> SettingsContent()
                    Section.PERFIL -> ProfileContent() // <- Perfil dentro del Drawer
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

@Composable private fun HomeContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Bienvenido a BikeLab 🚴‍♂️") }

@Composable private fun LocationContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Pantalla de Ubicación 📍") }

@Composable private fun NotificationsContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Notificaciones 📢") }

@Composable private fun SettingsContent() =
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Configuración ⚙️") }
