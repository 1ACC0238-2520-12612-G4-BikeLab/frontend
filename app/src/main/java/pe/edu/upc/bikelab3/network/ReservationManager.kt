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
}
