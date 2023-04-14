package br.com.trabalhofinalviacepandroid.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

class RetrofitInstance {
    companion object {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_VIACEP)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build() // Cria o Retrofit Object
            .create(ViaCepApiService::class.java)
    }
}