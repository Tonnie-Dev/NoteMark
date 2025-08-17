@file:RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Configuration
import com.tonyxlab.notemark.di.connectivityModule
import com.tonyxlab.notemark.di.dataStoreModule
import com.tonyxlab.notemark.di.databaseModule
import com.tonyxlab.notemark.di.networkModule
import com.tonyxlab.notemark.di.repositoryModule
import com.tonyxlab.notemark.di.syncWorkModule
import com.tonyxlab.notemark.di.useCasesModule
import com.tonyxlab.notemark.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.factory.KoinWorkerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class NoteMarkApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@NoteMarkApp)
            modules(
                    listOf(
                            viewModelModule,
                            repositoryModule,
                            networkModule,
                            databaseModule,
                            useCasesModule,
                            connectivityModule,
                            syncWorkModule,
                            dataStoreModule
                    )
            )
        }
    }
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
                .setWorkerFactory(KoinWorkerFactory())
                .build()

}