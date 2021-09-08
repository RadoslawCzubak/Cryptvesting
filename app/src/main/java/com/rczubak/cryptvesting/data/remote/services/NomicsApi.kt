package com.rczubak.cryptvesting.data.remote.services

import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NomicsApi {
    @GET("/v1/currencies/ticker")
    suspend fun getCryptocurrenciesCurrentInfo(
        @Query("ids") ids: String,
        @Query("interval") interval: String = "1h"
    ): Response<List<CryptoCurrencyModel>>
}