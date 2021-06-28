package com.rczubak.cryptvesting.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rczubak.cryptvesting.R
import com.rczubak.cryptvesting.utils.TransactionCalculator
import com.rczubak.cryptvesting.utils.XlsReader
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.InputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
   }

    override fun onStart() {
        super.onStart()
    }
}