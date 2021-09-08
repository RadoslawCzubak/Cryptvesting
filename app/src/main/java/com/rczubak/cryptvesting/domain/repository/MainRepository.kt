package com.rczubak.cryptvesting.domain.repository

import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.domain.model.Wallet

interface MainRepository {

    suspend fun getCurrentProfit(refresh: Boolean = true)

    suspend fun getWalletWithValue(): Resource<Wallet>
}