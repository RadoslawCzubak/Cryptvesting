package com.rczubak.cryptvesting.ui.dashboard

import com.rczubak.cryptvesting.data.models.domain.WalletCoin
import com.rczubak.cryptvesting.data.network.services.Resource

data class WalletUIModel(
    val walletCoins: Resource<ArrayList<WalletCoin>>,
    val walletValue: Resource<Double>
)