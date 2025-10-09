package pe.edu.upc.bikelab3.network

data class VehiculoRegistrado(
    val id: Int,
    val tipo: String, // "Scooter" o "Bicicleta"
    val marcaModelo: String,
    val precioPorHora: String,
    val precioPorDia: String,
    val ubicacionActual: String,
    val propietarioId: Int, // ID del arrendatario que lo registró
    val fechaRegistro: String,
    val disponible: Boolean = true
)
