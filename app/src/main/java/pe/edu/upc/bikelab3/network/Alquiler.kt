package pe.edu.upc.bikelab3.network

import java.util.Date

data class Alquiler(
    val id: Int,
    val bicicletaId: Int,
    val usuarioId: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val precioTotal: Double,
    val estado: String, // "ACTIVO", "COMPLETADO", "CANCELADO"
    val ubicacionRecogida: String,
    val ubicacionDevolucion: String,
    val notas: String = ""
)

