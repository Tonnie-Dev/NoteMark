package com.tonyxlab.notemark

import android.app.Application
import com.tonyxlab.notemark.presentation.di.appModule
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {

            modules(listOf(appModule))
        }
    }
}