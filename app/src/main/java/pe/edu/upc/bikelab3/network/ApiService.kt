package pe.edu.upc.bikelab3.network

import retrofit2.http.GET

interface ApiService {
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>
}
