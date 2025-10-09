package pe.edu.upc.bikelab3.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object ReseñaJsonReader {
    private var reseñas: List<Reseña> = emptyList()
    private var isLoaded = false

    fun loadReseñas(context: Context) {
        if (isLoaded) return
        
        try {
            val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val jsonMap: Map<String, Any> = gson.fromJson(jsonString, type)
            
            val reseñasJson = jsonMap["reseñas"] as? List<Map<String, Any>>
            reseñas = reseñasJson?.map { reseñaMap ->
                Reseña(
                    id = (reseñaMap["id"] as? Double)?.toInt() ?: 0,
                    vehiculoId = (reseñaMap["vehiculoId"] as? Double)?.toInt() ?: 0,
                    usuarioId = (reseñaMap["usuarioId"] as? Double)?.toInt() ?: 0,
                    rating = (reseñaMap["rating"] as? Double)?.toInt() ?: 5,
                    titulo = reseñaMap["titulo"] as? String ?: "",
                    comentario = reseñaMap["comentario"] as? String ?: "",
                    fecha = reseñaMap["fecha"] as? String ?: "",
                    nombreUsuario = reseñaMap["nombreUsuario"] as? String ?: "",
                    fotoUsuario = reseñaMap["fotoUsuario"] as? String ?: ""
                )
            } ?: emptyList()
            
            isLoaded = true
        } catch (e: Exception) {
            e.printStackTrace()
            reseñas = emptyList()
        }
    }

    fun getReseñasPorVehiculo(vehiculoId: Int): List<Reseña> {
        return reseñas.filter { it.vehiculoId == vehiculoId }
    }

    fun getReseñasPorPropietario(propietarioId: Int): List<Reseña> {
        // Obtener vehículos del propietario y luego sus reseñas
        val vehiculosDelPropietario = VehiculoJsonReader.getVehiculosPorPropietario(propietarioId)
        val vehiculoIds = vehiculosDelPropietario.map { it.id }
        
        return reseñas.filter { it.vehiculoId in vehiculoIds }
    }

    fun getReseñaPorId(reseñaId: Int): Reseña? {
        return reseñas.find { it.id == reseñaId }
    }

    fun getAllReseñas(): List<Reseña> {
        return reseñas
    }
}
