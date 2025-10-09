package pe.edu.upc.bikelab3.network

data class AlquilerActivo(
    val id: Int,
    val vehiculoId: Int,
    val usuarioId: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val precioPorHora: String,
    val precioTotal: String,
    val estado: String, // "ACTIVO", "FINALIZADO", "CANCELADO"
    val ubicacionRecogida: String,
    val ubicacionDevolucion: String,
    val tiempoInicioActivo: Long,
    val notas: String = ""
)
