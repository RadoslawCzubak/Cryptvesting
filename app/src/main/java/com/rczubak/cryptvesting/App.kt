package com.rczubak.cryptvesting

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
        setupCoil()
    }

    private fun setupCoil() {
        val imageLoader = ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .componentRegistry {
                add(SvgDecoder(applicationContext))
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}