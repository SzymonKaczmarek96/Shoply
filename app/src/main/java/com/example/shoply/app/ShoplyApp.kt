package com.example.shoply.app

import android.app.Application
import com.example.shoply.koin.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class ShoplyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@ShoplyApp)
            modules(appModule)
        }
    }
}