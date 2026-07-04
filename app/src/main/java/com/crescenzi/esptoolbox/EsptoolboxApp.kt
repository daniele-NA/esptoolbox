package com.crescenzi.esptoolbox

import android.app.Application
import com.crescenzi.esptoolbox.di.repositories
import com.crescenzi.esptoolbox.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EsptoolboxApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@EsptoolboxApp)
            modules(repositories, viewModels)
        }
    }
}
