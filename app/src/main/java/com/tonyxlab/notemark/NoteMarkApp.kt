package com.tonyxlab.notemark

import android.app.Application
import com.tonyxlab.notemark.di.networkModule
import com.tonyxlab.notemark.di.repositoryModule
import com.tonyxlab.notemark.di.viewModelModule
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        startKoin {
            modules(listOf(viewModelModule, repositoryModule, networkModule))
        }
    }
}