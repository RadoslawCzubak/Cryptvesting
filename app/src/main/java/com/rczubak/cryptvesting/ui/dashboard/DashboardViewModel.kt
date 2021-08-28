package com.rczubak.cryptvesting.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rczubak.cryptvesting.data.models.domain.WalletCoin
import com.rczubak.cryptvesting.data.network.services.Resource
import com.rczubak.cryptvesting.data.repository.MainRepository
import com.rczubak.cryptvesting.data.repository.TransactionsRepository
import com.rczubak.cryptvesting.utils.TransactionCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _walletCoins = MutableLiveData<Resource<ArrayList<WalletCoin>>>()
    val walletCoins: LiveData<Resource<ArrayList<WalletCoin>>> = _walletCoins
    private val _profit = MutableLiveData<Resource<Double>>()
    val profit: LiveData<Resource<Double>> = _profit

    fun getWallet() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val transactions = transactionsRepository.getAllTransactions()
                when (transactions.status) {
                    Resource.Status.SUCCESS -> {
                        val wallet = TransactionCalculator.calculateWallet(transactions.data!!)
                        _walletCoins.postValue(Resource.success(wallet))
                    }
                    Resource.Status.ERROR -> {
                        _walletCoins.postValue(Resource.error("Transactions error"))
                    }
                    Resource.Status.LOADING -> {
                        _walletCoins.postValue(Resource.loading())
                    }
                }
            }
        }
    }

    @InternalCoroutinesApi
    fun observeProfit() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                mainRepository.getCurrentProfit()
                mainRepository.profit.collect {
                    _profit.postValue(it)
                }
            }
        }
    }
}