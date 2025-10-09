package pe.edu.upc.bikelab3.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object LocalJsonReader {
    fun getUsuarios(context: Context): List<Usuario> {
        val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<Map<String, List<Usuario>>>() {}.type
        val data: Map<String, List<Usuario>> = Gson().fromJson(jsonString, type)
        return data["usuarios"] ?: emptyList()
    }
    
    fun getBicicletas(context: Context): List<Bicicleta> {
        val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<Map<String, List<Bicicleta>>>() {}.type
        val data: Map<String, List<Bicicleta>> = Gson().fromJson(jsonString, type)
        return data["bicicletas"] ?: emptyList()
    }
    
    fun getBicicletasDisponibles(context: Context): List<Bicicleta> {
        return getBicicletas(context).filter { it.disponible }
    }
    
    fun getProveedores(context: Context): List<Proveedor> {
        val jsonString = context.assets.open("db.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<Map<String, List<Proveedor>>>() {}.type
        val data: Map<String, List<Proveedor>> = Gson().fromJson(jsonString, type)
        return data["proveedores"] ?: emptyList()
    }
    
    fun getBicicletaConProveedor(context: Context, bicicletaId: Int): Pair<Bicicleta?, Proveedor?> {
        val bicicletas = getBicicletas(context)
        val proveedores = getProveedores(context)
        
        val bicicleta = bicicletas.find { it.id == bicicletaId }
        val proveedor = bicicleta?.let { 
            proveedores.find { proveedor -> proveedor.id == it.proveedorId }
        }
        
        return Pair(bicicleta, proveedor)
    }
    
    fun getUsuarioPorId(context: Context, usuarioId: Int): Usuario? {
        val usuarios = getUsuarios(context)
        return usuarios.find { it.id == usuarioId }
    }
}
