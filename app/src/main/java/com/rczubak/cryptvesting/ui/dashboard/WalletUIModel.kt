package com.rczubak.cryptvesting.ui.dashboard

import com.rczubak.cryptvesting.domain.model.WalletCoin
import com.rczubak.cryptvesting.common.Resource

data class WalletUIModel(
    val walletCoins: Resource<ArrayList<WalletCoin>>,
    val walletValue: Resource<Double>
)