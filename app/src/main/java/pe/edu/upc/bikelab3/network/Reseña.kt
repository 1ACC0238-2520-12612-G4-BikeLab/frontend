package pe.edu.upc.bikelab3.network

data class Reseña(
    val id: Int,
    val vehiculoId: Int,
    val usuarioId: Int,
    val rating: Int, // 1-5 estrellas
    val titulo: String,
    val comentario: String,
    val fecha: String,
    val nombreUsuario: String,
    val fotoUsuario: String
)
