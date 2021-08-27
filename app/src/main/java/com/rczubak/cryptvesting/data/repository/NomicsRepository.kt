package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.dao.cryptocurrencyinfo.CryptocurrenciesDao
import com.rczubak.cryptvesting.data.dao.transactions.TransactionsDao
import com.rczubak.cryptvesting.data.models.entities.CryptocurrencyEntity
import com.rczubak.cryptvesting.data.network.services.NomicsApi
import com.rczubak.cryptvesting.utils.prepareCommaSeparatedQueryParameters
import timber.log.Timber
import javax.inject.Inject

class NomicsRepository @Inject constructor(
    private val api: NomicsApi,
    private val transactionsDao: TransactionsDao,
    private val cryptocurrenciesDao: CryptocurrenciesDao
) {
    suspend fun syncStatesOfAllOwnedCrypto() {
        val ownedCryptoSymbols = transactionsDao.getOwnedCrypto()
        val idsQueryParam = prepareCommaSeparatedQueryParameters(ownedCryptoSymbols)
        val response = api.getCryptocurrenciesCurrentInfo(idsQueryParam)
        if (response.isSuccessful && response.body()?.isNotEmpty() == true) {
            val cryptocurrencies = response.body()
            cryptocurrenciesDao.insertAll(cryptocurrencies!!.map {
                CryptocurrencyEntity(it)
            })
        }
        Timber.d(cryptocurrenciesDao.getAllCryptocurrenciesState().toString())
    }
}