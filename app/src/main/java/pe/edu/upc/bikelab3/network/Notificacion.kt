package pe.edu.upc.bikelab3.network

data class Notificacion(
    val id: Int,
    val remitente: String,
    val mensaje: String,
    val timestamp: String,
    val tipo: String, // "USUARIO", "BIKELAB", "SISTEMA"
    val avatar: String = "", // URL o referencia al avatar
    val leida: Boolean = false
)
