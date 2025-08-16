@file:RequiresApi(Build.VERSION_CODES.O)
@file:RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)

package com.tonyxlab.notemark


import android.app.Application
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.data.workmanager.SyncWorker
import com.tonyxlab.notemark.di.connectivityModule
import com.tonyxlab.notemark.di.databaseModule
import com.tonyxlab.notemark.di.networkModule
import com.tonyxlab.notemark.di.repositoryModule
import com.tonyxlab.notemark.di.syncWorkModule
import com.tonyxlab.notemark.di.useCasesModule
import com.tonyxlab.notemark.di.viewModelModule
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import timber.log.Timber

class NoteMarkApp : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

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
                            syncWorkModule
                    )
            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
                .setWorkerFactory(
                        object : WorkerFactory() {
                            override fun createWorker(
                                appContext: Context,
                                workerClassName: String,
                                workerParameters: WorkerParameters
                            ): ListenableWorker? =
                                when (Class.forName(workerClassName)) {
                                    SyncWorker::class.java -> getKoin().get<SyncWorker> {
                                        parametersOf(appContext, workerParameters)
                                    }

                                    else -> null
                                }
                        }

                )
                .build()
}