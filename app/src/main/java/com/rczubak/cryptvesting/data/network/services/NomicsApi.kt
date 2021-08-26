package com.rczubak.cryptvesting.data.network.services

import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NomicsApi {
    @GET("/currencies/ticker")
    suspend fun getCryptocurrenciesCurrentInfo(@Query("ids") ids: String): Response<List<CryptoCurrencyModel>>

    @GET("/currencies/ticker")
    fun getCryptocurrenciesCurrentInfoPaged(
        @Query("ids") ids: String,
        @Query("page") page: Int,
        @Query("per-page") per_page: Int = 50
    )
}