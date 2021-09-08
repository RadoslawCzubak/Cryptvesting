package com.rczubak.cryptvesting.data.repository

import com.rczubak.cryptvesting.domain.model.CryptoCurrencyModel
import org.threeten.bp.LocalDateTime

class FakeCryptoMarketRepository {

    fun getCoinsPrice(symbolList: ArrayList<String>): ArrayList<CryptoCurrencyModel>{
        return arrayListOf(
            CryptoCurrencyModel("Bitcoin","BTC", 38680.0 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("Ethereum","ETH", 2747.5 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("Burger Swap","BURGER", 7.75 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("DogeCoin","DOGE", 0.3341 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("BitTorrent Token","BTT", 0.0041602 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("pNetwork","PNT", 1.14 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("Loopring","LRC", 0.40265 , LocalDateTime.now() , "USDT"),
            CryptoCurrencyModel("Binance","BNB", 369.71 , LocalDateTime.now() , "USDT"),
        )
    }
}