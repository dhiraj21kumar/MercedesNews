package com.example.mercedesnews.ui

import android.app.Application
import com.example.mercedesnews.util.newsAdapterModule
import com.example.mercedesnews.util.viewModelModule
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            modules(listOf(viewModelModule, newsAdapterModule))
        }
    }
}