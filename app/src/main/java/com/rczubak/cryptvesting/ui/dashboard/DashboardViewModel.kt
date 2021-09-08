package com.rczubak.cryptvesting.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rczubak.cryptvesting.domain.model.Wallet
import com.rczubak.cryptvesting.common.Resource
import com.rczubak.cryptvesting.data.repository.NomicsRepository
import com.rczubak.cryptvesting.data.repository.TransactionsRepository
import com.rczubak.cryptvesting.domain.repository.MainRepository
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
    private val mainRepository: MainRepository,
    private val nomicsRepository: NomicsRepository
) : ViewModel() {

    private val _wallet = MutableLiveData<Resource<Wallet>>()
    val wallet: LiveData<Resource<Wallet>> = _wallet
    private val _profit = MutableLiveData<Resource<Double>>()
    val profit: LiveData<Resource<Double>> = _profit

    init {
        refreshCrypto()
    }

    private fun refreshCrypto(){
        viewModelScope.launch {
            nomicsRepository.getCryptoCurrenciesState(transactionsRepository.getOwnedCrypto().data!!)
            mainRepository.getCurrentProfit()
        }
    }

    fun getWallet() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val wallet = mainRepository.getWalletWithValue()
                when (wallet.status) {
                    Resource.Status.SUCCESS -> {
                        _wallet.postValue(
                            wallet
                        )
                    }
                    Resource.Status.ERROR -> {
                        _wallet.postValue(
                            Resource.error("Transactions error")
                        )
                    }
                    Resource.Status.LOADING -> {
                        Resource.loading<Wallet>()
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