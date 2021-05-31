package com.rczubak.cryptvesting.data.network.services

import retrofit2.http.GET
import retrofit2.http.Query

interface NomicsApi {
    @GET("/currencies/ticker")
    fun getCryptocurrenciesCurrentInfo(@Query("ids")ids: List<String> )

    @GET("/currencies/ticker")
    fun getCryptocurrenciesCurrentInfoPaged(@Query("ids")ids: List<String>, @Query("page") page: Int, @Query("per-page") per_page: Int = 50 )
}