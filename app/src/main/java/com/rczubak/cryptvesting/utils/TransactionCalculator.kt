package com.rczubak.cryptvesting.utils

import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.data.models.domain.TransactionType
import com.rczubak.cryptvesting.data.repository.FakeCryptoMarketRepository

object TransactionCalculator {
    fun calculateProfitInUSD(transactions: ArrayList<TransactionModel>): Double {
        val transactionsCoins = transactions.map {
            it.buyCoin
        }
        val prices =
            FakeCryptoMarketRepository().getCoinsPrice(ArrayList(transactionsCoins.distinct()))
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
        var moneyInCryptoNow = 0.0
        for((key, value) in cryptos){
            val price = prices.find {
                it.symbol == key
            }?.price
            moneyInCryptoNow += value * price!!
        }
        return moneyInCryptoNow - moneySpentOnCurrentCrypto
    }
}