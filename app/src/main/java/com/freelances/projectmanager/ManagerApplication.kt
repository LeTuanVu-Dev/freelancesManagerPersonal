package com.freelances.projectmanager

import android.app.Application
import com.freelances.projectmanager.di.appModule
import com.freelances.projectmanager.di.viewModelModule
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ManagerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        startKoin {
            androidLogger()
            androidContext(this@ManagerApplication)
            modules(
                appModule, viewModelModule
            )
        }
    }
}