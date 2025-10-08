package pe.edu.upc.bikelab3.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.bikelab3.network.UserSession

@Composable
fun ProfileContent() {
    val user = UserSession.currentUser
    if (user == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay sesión activa", color = MaterialTheme.colorScheme.error)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${user.nombre} ${user.apellido}",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = user.correo, style = MaterialTheme.typography.bodyMedium)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ProfileItem("Universidad", user.universidad)
                ProfileItem("Código", user.codigo)
                ProfileItem("Número", user.numero)
                ProfileItem("Dirección", user.direccion)
                ProfileItem("Método de Pago", user.metodoPago)
                ProfileItem("Viajes Totales", user.viajes.toString())
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(value, fontSize = 14.sp)
    }
}
