package br.com.trabalhofinalviacepandroid.service

import br.com.trabalhofinalviacepandroid.dataclass.ViaCepResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepApiService {
    @GET("ws/{cep}/json")
    suspend fun getCep(@Path("cep")cep:String): Response<ViaCepResponse>
}