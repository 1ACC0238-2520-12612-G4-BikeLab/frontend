package pe.edu.upc.bikelab3.network

data class Bicicleta(
    val id: Int,
    val proveedorId: Int,
    val modelo: String,
    val marca: String,
    val tipo: String,
    val precioPorHora: Double,
    val ubicacion: String,
    val disponible: Boolean,
    val rating: Double,
    val descripcion: String,
    val imagen: String
)
