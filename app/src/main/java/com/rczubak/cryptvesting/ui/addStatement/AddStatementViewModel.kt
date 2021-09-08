package com.rczubak.cryptvesting.ui.addStatement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rczubak.cryptvesting.domain.model.TransactionModel
import com.rczubak.cryptvesting.data.repository.TransactionsRepository
import com.rczubak.cryptvesting.common.Event
import com.rczubak.cryptvesting.common.XlsReader
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AddStatementViewModel @Inject constructor(
    private val transactionsRepository: TransactionsRepository
) : ViewModel() {

    private val _transactionsToAdd = MutableLiveData<ArrayList<TransactionModel>>()
    val transactionsToAdd: LiveData<ArrayList<TransactionModel>> = _transactionsToAdd
    private val _transactionsSaveStatus = MutableLiveData<Event<Boolean>>()
    val transactionsSaveStatus: LiveData<Event<Boolean>> = _transactionsSaveStatus

    fun readFile(inputStream: InputStream) {
        val readedTransactions = XlsReader().readTransactionsFromXlsx(inputStream)
        _transactionsToAdd.postValue(readedTransactions)
    }

    fun saveTransactions(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (transactionsToAdd.value != null) {
                    transactionsRepository.addAllTransactions(transactionsToAdd.value!!)
                    _transactionsSaveStatus.postValue(Event(true))
                }
            }
        }
    }
}