package pe.edu.upc.bikelab3.network

import java.util.Date

data class Alquiler(
    val id: Int,
    val bicicletaId: Int,
    val usuarioId: Int,
    val fechaInicio: String,
    val fechaFin: String,
    val precioPorHora: Double,
    val estado: String, // "PENDIENTE", "ACTIVO", "COMPLETADO", "CANCELADO"
    val ubicacionRecogida: String,
    val ubicacionDevolucion: String,
    val notas: String = "",
    val tiempoInicioActivo: Long = 0L, // Timestamp cuando se activó el alquiler
    val precioTotal: Double = 0.0 // Precio total calculado dinámicamente
)

