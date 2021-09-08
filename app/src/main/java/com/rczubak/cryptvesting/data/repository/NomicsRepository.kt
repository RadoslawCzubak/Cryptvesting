package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.data.database.cryptocurrencyinfo.CryptocurrenciesDao
import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import com.rczubak.cryptvesting.data.database.entities.CryptocurrencyEntity
import com.rczubak.cryptvesting.data.remote.services.NomicsApi
import com.rczubak.cryptvesting.utils.Resource
import com.rczubak.cryptvesting.utils.prepareCommaSeparatedQueryParameters
import timber.log.Timber
import javax.inject.Inject

class NomicsRepository @Inject constructor(
    private val api: NomicsApi,
    private val cryptocurrenciesDao: CryptocurrenciesDao
) {
    suspend fun syncStatesOfAllOwnedCrypto(ownedCryptoSymbols: List<String>) {
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

    suspend fun getCryptoCurrenciesState(
        cryptoSymbols: List<String>,
        refresh: Boolean = false
    ): Resource<List<CryptoCurrencyModel>> {
        if (refresh) {
            syncStatesOfAllOwnedCrypto(cryptoSymbols)
        }
        var cryptoStates = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(cryptoSymbols)
        if (cryptoStates.isEmpty()) {
            syncStatesOfAllOwnedCrypto(cryptoSymbols)
            cryptoStates = cryptocurrenciesDao.getCryptoCurrenciesStateBySymbol(cryptoSymbols)
        }
        return Resource.success(cryptoStates.map { CryptoCurrencyModel(it) })
    }

}