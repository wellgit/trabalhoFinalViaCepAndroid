package br.com.trabalhofinalviacepandroid.dataclass

import com.squareup.moshi.Json

data class ViaCepResponse(
    @Json(name = "bairro")
    val bairro: String,
    @Json(name = "cep")
    val cep: String,
    @Json(name = "complemento")
    val complemento: String,
    @Json(name = "ddd")
    val ddd: String,
    @Json(name = "gia")
    val gia: String,
    @Json(name = "ibge")
    val ibge: String,
    @Json(name = "localidade")
    val localidade: String,
    @Json(name = "logradouro")
    val logradouro: String,
    @Json(name = "siafi")
    val siafi: String,
    @Json(name = "uf")
    val uf: String
)
