package com.rczubak.cryptvesting.ui.addStatement

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.rczubak.cryptvesting.utils.XlsReader
import timber.log.Timber
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class AddStatementViewModel @Inject constructor() : ViewModel() {

    fun readFile(inputStream: InputStream){
        val read = XlsReader().readTransactionsFromXlsx(inputStream)
        Timber.d(read.toString())
    }
}