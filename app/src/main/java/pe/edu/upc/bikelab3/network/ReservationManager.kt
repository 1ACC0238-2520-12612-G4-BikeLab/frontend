package pe.edu.upc.bikelab3.network

object ReservationManager {
    private val reservedBikes = mutableSetOf<Int>()
    private val userRentals = mutableListOf<Alquiler>()
    
    fun reserveBike(bikeId: Int) {
        reservedBikes.add(bikeId)
    }
    
    fun isReserved(bikeId: Int): Boolean {
        return reservedBikes.contains(bikeId)
    }
    
    fun getAvailableBikes(allBikes: List<Bicicleta>): List<Bicicleta> {
        return allBikes.filter { !isReserved(it.id) }
    }
    
    fun clearReservations() {
        reservedBikes.clear()
    }
    
    fun getReservedCount(): Int {
        return reservedBikes.size
    }
    
    // Funciones para manejar alquileres
    fun addRental(alquiler: Alquiler) {
        userRentals.add(alquiler)
    }
    
    fun getUserRentals(): List<Alquiler> {
        return userRentals.toList()
    }
    
    fun clearRentals() {
        userRentals.clear()
    }
    
    fun getActiveRentals(): List<Alquiler> {
        return userRentals.filter { it.estado == "ACTIVO" }
    }
    
    fun getCompletedRentals(): List<Alquiler> {
        return userRentals.filter { it.estado == "COMPLETADO" }
    }
    
    fun getPendingRentals(): List<Alquiler> {
        return userRentals.filter { it.estado == "PENDIENTE" }
    }
    
    fun activateRental(alquilerId: Int): Boolean {
        val alquiler = userRentals.find { it.id == alquilerId }
        return if (alquiler != null && alquiler.estado == "PENDIENTE") {
            val index = userRentals.indexOf(alquiler)
            val alquilerActivado = alquiler.copy(
                estado = "ACTIVO",
                tiempoInicioActivo = System.currentTimeMillis()
            )
            userRentals[index] = alquilerActivado
            true
        } else {
            false
        }
    }
    
    fun finalizeRental(alquilerId: Int): Boolean {
        val alquiler = userRentals.find { it.id == alquilerId }
        return if (alquiler != null && alquiler.estado == "ACTIVO") {
            val index = userRentals.indexOf(alquiler)
            val tiempoTranscurrido = System.currentTimeMillis() - alquiler.tiempoInicioActivo
            val horasUsadas = tiempoTranscurrido / (1000 * 60 * 60.0) // Convertir a horas
            val precioFinal = alquiler.precioPorHora * horasUsadas
            
            val alquilerFinalizado = alquiler.copy(
                estado = "COMPLETADO",
                precioTotal = precioFinal
            )
            userRentals[index] = alquilerFinalizado
            true
        } else {
            false
        }
    }
}
