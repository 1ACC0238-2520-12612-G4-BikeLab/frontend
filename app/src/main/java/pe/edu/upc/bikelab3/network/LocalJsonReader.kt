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
}
