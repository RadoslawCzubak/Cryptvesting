package com.rczubak.cryptvesting.ui.addStatement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rczubak.cryptvesting.data.models.domain.TransactionModel
import com.rczubak.cryptvesting.utils.XlsReader
import java.io.InputStream
import javax.inject.Inject

class AddStatementViewModel @Inject constructor() : ViewModel() {

    private val _transactionsToAdd = MutableLiveData<ArrayList<TransactionModel>>()
    val transactionsToAdd: LiveData<ArrayList<TransactionModel>> = _transactionsToAdd

    fun readFile(inputStream: InputStream) {
        val readedTransactions = XlsReader().readTransactionsFromXlsx(inputStream)
        _transactionsToAdd.postValue(readedTransactions)
    }
}