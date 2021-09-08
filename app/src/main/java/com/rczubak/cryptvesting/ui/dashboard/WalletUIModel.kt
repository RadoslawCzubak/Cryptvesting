package com.rczubak.cryptvesting.ui.dashboard

import com.rczubak.cryptvesting.domain.model.WalletCoin
import com.rczubak.cryptvesting.utils.Resource

data class WalletUIModel(
    val walletCoins: Resource<ArrayList<WalletCoin>>,
    val walletValue: Resource<Double>
)