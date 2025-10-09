package pe.edu.upc.bikelab3.network

object NotificationManager {
    private val _notifications = mutableListOf<Notificacion>()
    
    // Notificaciones predefinidas
    private val predefinedNotifications = listOf(
        Notificacion(
            id = 1,
            remitente = "pedro suarez - UPC SAN MIG",
            mensaje = "Hola, ya me encuentro en la ubicación acordada",
            timestamp = "9:41 AM",
            tipo = "USUARIO",
            avatar = "green_creature"
        ),
        Notificacion(
            id = 2,
            remitente = "Pedro Sanches - UPC SAN MIG",
            mensaje = "Gracias por la ayuda, te voy a recompensar",
            timestamp = "9:41 AM",
            tipo = "USUARIO",
            avatar = "vr_person"
        ),
        Notificacion(
            id = 3,
            remitente = "BIKELAB - CORP",
            mensaje = "Te agradecemos el preferirnos para tus viajes",
            timestamp = "9:41 AM",
            tipo = "BIKELAB"
        ),
        Notificacion(
            id = 4,
            remitente = "BIKELAB - CORP",
            mensaje = "Mejora tu seguridad utilizando nuestros servicios",
            timestamp = "9:41 AM",
            tipo = "BIKELAB"
        )
    )
    
    init {
        _notifications.addAll(predefinedNotifications)
    }
    
    fun getAllNotifications(): List<Notificacion> {
        return _notifications.toList()
    }
    
    fun getUnreadCount(): Int {
        return _notifications.count { !it.leida }
    }
    
    fun markAsRead(notificationId: Int) {
        val index = _notifications.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            _notifications[index] = _notifications[index].copy(leida = true)
        }
    }
    
    fun addNotification(notification: Notificacion) {
        _notifications.add(0, notification) // Agregar al inicio
    }
    
    fun deleteNotification(notificationId: Int) {
        _notifications.removeAll { it.id == notificationId }
    }
    
    fun clearNotifications() {
        _notifications.clear()
        _notifications.addAll(predefinedNotifications)
    }
    
    // Función para crear notificación de reserva
    fun createReservationNotification(bicicletaModelo: String, proveedorNombre: String): Notificacion {
        val currentTime = java.text.SimpleDateFormat("h:mm a", java.util.Locale.getDefault())
            .format(java.util.Date())
        
        return Notificacion(
            id = System.currentTimeMillis().toInt(),
            remitente = "BIKELAB - SISTEMA",
            mensaje = "Tu reserva de $bicicletaModelo con $proveedorNombre ha sido confirmada",
            timestamp = currentTime,
            tipo = "SISTEMA"
        )
    }
}
