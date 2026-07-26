package com.crescenzi.esptoolbox

import android.app.Application
import com.crescenzi.esptoolbox.di.repositories
import com.crescenzi.esptoolbox.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(repositories, viewModels)
        }
    }
}
