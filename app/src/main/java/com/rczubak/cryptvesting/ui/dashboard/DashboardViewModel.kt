package com.rczubak.cryptvesting.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rczubak.cryptvesting.data.models.domain.WalletCoin
import com.rczubak.cryptvesting.data.repository.TransactionsRepository
import com.rczubak.cryptvesting.utils.TransactionCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TransactionsRepository
) : ViewModel() {

    private val _walletCoins = MutableLiveData<ArrayList<WalletCoin>>()
    val walletCoins: LiveData<ArrayList<WalletCoin>> = _walletCoins

    fun getWallet() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val transactions = repository.getAllTransactions()
                val wallet = TransactionCalculator.calculateWallet(transactions)
                _walletCoins.postValue(wallet)
            }
        }
    }
}