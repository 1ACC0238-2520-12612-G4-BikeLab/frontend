package pe.edu.upc.bikelab3.network

data class Usuario(
    val id: Int,
    val usuario: String,
    val contrasena: String,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val universidad: String,
    val codigo: String,
    val numero: String,
    val direccion: String,
    val metodoPago: String,
    val viajes: Int,
    val tipo: String = "Ciclista" // Por defecto es Ciclista
)
