package com.rczubak.cryptvesting.data.network.services

import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NomicsApi {
    @GET("/v1/currencies/ticker")
    suspend fun getCryptocurrenciesCurrentInfo(@Query("ids") ids: String): Response<List<CryptoCurrencyModel>>
}