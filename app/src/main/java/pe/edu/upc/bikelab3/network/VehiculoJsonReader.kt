package pe.edu.upc.bikelab3.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import pe.edu.upc.bikelab3.R

object VehiculoJsonReader {
    private var vehiculosRegistrados: List<VehiculoRegistrado> = emptyList()
    private var isLoaded = false

    fun loadVehiculos(context: Context) {
        if (isLoaded) return
        
        try {
            val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
            val gson = Gson()
            val type = object : TypeToken<Map<String, Any>>() {}.type
            val jsonMap: Map<String, Any> = gson.fromJson(jsonString, type)
            
            val vehiculosJson = jsonMap["vehiculosRegistrados"] as? List<Map<String, Any>>
            vehiculosRegistrados = vehiculosJson?.map { vehiculoMap ->
                VehiculoRegistrado(
                    id = (vehiculoMap["id"] as? Double)?.toInt() ?: 0,
                    propietarioId = (vehiculoMap["propietarioId"] as? Double)?.toInt() ?: 0,
                    tipo = vehiculoMap["tipo"] as? String ?: "",
                    marcaModelo = vehiculoMap["marcaModelo"] as? String ?: "",
                    precioPorHora = vehiculoMap["precioPorHora"] as? String ?: "0.0",
                    precioPorDia = vehiculoMap["precioPorDia"] as? String ?: "0.0",
                    ubicacionActual = vehiculoMap["ubicacionActual"] as? String ?: "",
                    fechaRegistro = vehiculoMap["fechaRegistro"] as? String ?: "",
                    disponible = vehiculoMap["disponible"] as? Boolean ?: true
                )
            } ?: emptyList()
            
            isLoaded = true
        } catch (e: Exception) {
            e.printStackTrace()
            vehiculosRegistrados = emptyList()
        }
    }

    fun getVehiculosPorPropietario(propietarioId: Int): List<VehiculoRegistrado> {
        return vehiculosRegistrados.filter { it.propietarioId == propietarioId }
    }

    fun getVehiculoPorId(vehiculoId: Int): VehiculoRegistrado? {
        return vehiculosRegistrados.find { it.id == vehiculoId }
    }

    fun getAllVehiculos(): List<VehiculoRegistrado> {
        return vehiculosRegistrados
    }
}
