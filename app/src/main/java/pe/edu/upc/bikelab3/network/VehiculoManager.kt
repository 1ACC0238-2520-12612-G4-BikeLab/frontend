package pe.edu.upc.bikelab3.network

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object VehiculoManager {
    private val _vehiculosRegistrados = mutableListOf<VehiculoRegistrado>()
    private var context: Context? = null
    
    fun initialize(context: Context) {
        this.context = context
        VehiculoJsonReader.loadVehiculos(context)
        AlquilerActivoJsonReader.loadAlquileresActivos(context)
        ReseñaJsonReader.loadReseñas(context)
        // Cargar vehículos desde JSON al inicializar
        _vehiculosRegistrados.clear()
        _vehiculosRegistrados.addAll(VehiculoJsonReader.getAllVehiculos())
    }
    
    fun agregarVehiculo(vehiculo: VehiculoRegistrado) {
        _vehiculosRegistrados.add(vehiculo)
    }
    
    fun getVehiculosPorPropietario(propietarioId: Int): List<VehiculoRegistrado> {
        return _vehiculosRegistrados.filter { it.propietarioId == propietarioId }
    }
    
    fun getAllVehiculos(): List<VehiculoRegistrado> {
        return _vehiculosRegistrados.toList()
    }
    
    fun eliminarVehiculo(vehiculoId: Int) {
        _vehiculosRegistrados.removeAll { it.id == vehiculoId }
    }
    
    fun crearVehiculo(
        tipo: String,
        marcaModelo: String,
        precioPorHora: String,
        precioPorDia: String,
        ubicacionActual: String,
        propietarioId: Int
    ): VehiculoRegistrado {
        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        
        return VehiculoRegistrado(
            id = System.currentTimeMillis().toInt(),
            tipo = tipo,
            marcaModelo = marcaModelo,
            precioPorHora = precioPorHora,
            precioPorDia = precioPorDia,
            ubicacionActual = ubicacionActual,
            propietarioId = propietarioId,
            fechaRegistro = fechaActual,
            disponible = true
        )
    }
    
    fun getVehiculoPorId(vehiculoId: Int): VehiculoRegistrado? {
        return _vehiculosRegistrados.find { it.id == vehiculoId }
    }
    
    fun actualizarVehiculo(vehiculoActualizado: VehiculoRegistrado) {
        val index = _vehiculosRegistrados.indexOfFirst { it.id == vehiculoActualizado.id }
        if (index != -1) {
            _vehiculosRegistrados[index] = vehiculoActualizado
        }
    }
}
