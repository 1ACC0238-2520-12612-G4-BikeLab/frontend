package pe.edu.upc.bikelab3.network

data class Proveedor(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val telefono: String,
    val direccion: String,
    val rating: Double,
    val bicicletasRegistradas: Int,
    val fechaRegistro: String,
    val imagenPerfil: String = "default_profile.jpg"
)
