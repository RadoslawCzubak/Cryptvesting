package com.rczubak.cryptvesting.utils

import com.rczubak.cryptvesting.data.models.domain.CryptoCurrencyModel
import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.data.models.domain.TransactionType
import com.rczubak.cryptvesting.data.models.domain.WalletCoin

object TransactionCalculator {
    fun calculateProfitInUSD(
        transactions: ArrayList<TransactionModel>,
        prices: ArrayList<CryptoCurrencyModel>
    ): Double {
        var moneySpentOnCurrentCrypto = 0.0
        val cryptos = mutableMapOf<String, Double>()
        for (transaction in transactions) {
            if (transaction.type == TransactionType.BUY) {
                moneySpentOnCurrentCrypto -= transaction.getTotalPrice()
                if (cryptos.keys.contains(transaction.buyCoin)) {
                    cryptos[transaction.buyCoin] =
                        cryptos[transaction.buyCoin]!!.plus(transaction.amount)
                } else {
                    cryptos[transaction.buyCoin] = transaction.amount
                }
            }
            if (transaction.type == TransactionType.SELL) {
                moneySpentOnCurrentCrypto += transaction.getTotalPrice()
                if (cryptos.keys.contains(transaction.buyCoin)) {
                    cryptos[transaction.buyCoin] =
                        cryptos[transaction.buyCoin]!!.minus(transaction.amount)
                } else {
                    cryptos[transaction.buyCoin] = transaction.amount
                }
            }
        }
        val moneyInCryptoNow = calculateCryptoValue(cryptos, prices)
        return moneyInCryptoNow + moneySpentOnCurrentCrypto
    }

    fun calculateCryptoValue(
        cryptos: Map<String, Double>,
        prices: ArrayList<CryptoCurrencyModel>
    ): Double {
        var moneyInCryptoNow = 0.0
        for ((key, value) in cryptos) {
            val price = prices.find {
                it.symbol == key
            }?.price
            moneyInCryptoNow += value * price!!
        }
        return moneyInCryptoNow
    }

    fun calculateWalletCoinValues(
        walletCoins: List<WalletCoin>,
        prices: List<CryptoCurrencyModel>
    ): List<WalletCoin> {
        val walletCoinsWithUpdatedValues = ArrayList<WalletCoin>()
        for (coin in walletCoins) {
            val price = prices.find {
                it.symbol == coin.currencySymbol
            }?.price
            if (price != null) {
                walletCoinsWithUpdatedValues.add(
                    coin.copy(valueInUSD = (price * coin.amount))
                )
            } else {
                walletCoinsWithUpdatedValues.add(coin)
            }
        }
        return walletCoinsWithUpdatedValues.toList()
    }

    fun calculateWallet(transactions: ArrayList<TransactionModel>): ArrayList<WalletCoin> {
        val walletCoins = transactions.map {
            WalletCoin(it.amount, it.buyCoin)
        }
        val coinsSymbol = walletCoins.distinctBy {
            it.currencySymbol
        }.map {
            it.currencySymbol
        }
        val wallet = ArrayList<WalletCoin>()
        for (symbol in coinsSymbol) {
            val singleCoinList = walletCoins.filter {
                it.currencySymbol == symbol
            }
            var totalAmount = 0.0
            for (coin in singleCoinList) {
                totalAmount += coin.amount
            }
            wallet.add(WalletCoin(totalAmount, symbol))
        }
        return wallet
    }


}