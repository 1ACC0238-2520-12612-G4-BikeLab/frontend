package pe.edu.upc.bikelab3.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AlquilerActivoJsonReader {
    private var alquileresActivos: List<AlquilerActivo> = emptyList()
    private var isLoaded = false

    fun loadAlquileresActivos(context: Context) {
        if (isLoaded) return
        
        try {
            val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val jsonMap: Map<String, Any> = gson.fromJson(jsonString, type)
            
            val alquileresJson = jsonMap["vehiculosactivos"] as? List<Map<String, Any>>
            alquileresActivos = alquileresJson?.map { alquilerMap ->
                AlquilerActivo(
                    id = (alquilerMap["id"] as? Double)?.toInt() ?: 0,
                    vehiculoId = (alquilerMap["vehiculoId"] as? Double)?.toInt() ?: 0,
                    usuarioId = (alquilerMap["usuarioId"] as? Double)?.toInt() ?: 0,
                    fechaInicio = alquilerMap["fechaInicio"] as? String ?: "",
                    fechaFin = alquilerMap["fechaFin"] as? String ?: "",
                    precioPorHora = alquilerMap["precioPorHora"] as? String ?: "0.0",
                    precioTotal = alquilerMap["precioTotal"] as? String ?: "0.0",
                    estado = alquilerMap["estado"] as? String ?: "ACTIVO",
                    ubicacionRecogida = alquilerMap["ubicacionRecogida"] as? String ?: "",
                    ubicacionDevolucion = alquilerMap["ubicacionDevolucion"] as? String ?: "",
                    tiempoInicioActivo = (alquilerMap["tiempoInicioActivo"] as? Double)?.toLong() ?: 0L,
                    notas = alquilerMap["notas"] as? String ?: ""
                )
            } ?: emptyList()
            
            isLoaded = true
        } catch (e: Exception) {
            e.printStackTrace()
            alquileresActivos = emptyList()
        }
    }

    fun getAlquileresActivosPorPropietario(propietarioId: Int): List<AlquilerActivo> {
        // Filtrar alquileres activos que corresponden a vehículos del propietario
        val vehiculosDelPropietario = VehiculoJsonReader.getVehiculosPorPropietario(propietarioId)
        val vehiculoIds = vehiculosDelPropietario.map { it.id }
        
        return alquileresActivos.filter { it.vehiculoId in vehiculoIds }
    }

    fun getAlquilerActivoPorId(alquilerId: Int): AlquilerActivo? {
        return alquileresActivos.find { it.id == alquilerId }
    }

    fun getAllAlquileresActivos(): List<AlquilerActivo> {
        return alquileresActivos
    }
}
