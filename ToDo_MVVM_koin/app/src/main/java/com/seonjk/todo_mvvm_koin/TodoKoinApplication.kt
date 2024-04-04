package com.seonjk.todo_mvvm_koin

import android.app.Application
import com.seonjk.todo_mvvm_koin.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TodoKoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android Logger
            androidLogger()
            // Reference Android context
            androidContext(this@TodoKoinApplication)
            // Load modules
            modules(appModule)
        }
    }
}