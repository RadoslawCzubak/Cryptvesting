package com.rczubak.cryptvesting.domain.repository

import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.domain.model.Wallet
import kotlinx.coroutines.flow.SharedFlow

interface MainRepository {

    val profit: SharedFlow<Resource<Double>>

    suspend fun getCurrentProfit(refresh: Boolean = true)

    suspend fun getWalletWithValue(): Resource<Wallet>
}