@file: RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.di

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.tonyxlab.notemark.data.local.database.NoteMarkDatabase
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.data.network.HttpClientFactory
import com.tonyxlab.notemark.data.remote.auth.AuthRepositoryImpl
import com.tonyxlab.notemark.data.remote.connectivity.ConnectivityObserverImpl
import com.tonyxlab.notemark.data.repository.NoteRepositoryImpl
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.data.workmanager.SyncWorker
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.domain.usecase.DeleteNoteUseCase
import com.tonyxlab.notemark.domain.usecase.GetAllNotesUseCase
import com.tonyxlab.notemark.domain.usecase.GetNoteByIdUseCase
import com.tonyxlab.notemark.domain.usecase.LogOutUseCase
import com.tonyxlab.notemark.domain.usecase.UpsertNoteUseCase
import com.tonyxlab.notemark.presentation.screens.editor.EditorViewModel
import com.tonyxlab.notemark.presentation.screens.home.HomeViewModel
import com.tonyxlab.notemark.presentation.screens.landing.LandingViewModel
import com.tonyxlab.notemark.presentation.screens.login.LoginViewModel
import com.tonyxlab.notemark.presentation.screens.settings.SettingsViewModel
import com.tonyxlab.notemark.presentation.screens.signup.SignupViewModel
import com.tonyxlab.notemark.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::LoginViewModel)
    viewModelOf(::SignupViewModel)
    viewModelOf(::LandingViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::EditorViewModel)
    viewModelOf(::SettingsViewModel)
}

val networkModule = module {
    single {
        TokenStorage.init(androidContext())
        HttpClientFactory()
    }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
}

val connectivityModule = module {
    single<ConnectivityManager> {
        ContextCompat.getSystemService(
                androidContext(),
                ConnectivityManager::class.java
        ) ?: error("Connectivity Manager not available")

    }
    single<ConnectivityObserver> { ConnectivityObserverImpl(get()) }
}

val databaseModule = module {

    single<NoteMarkDatabase> {
        Room.databaseBuilder(
                context = androidContext(),
                klass = NoteMarkDatabase::class.java,
                name = Constants.DATABASE_NAME
        )
                .build()
    }
    single<NoteDao> { get<NoteMarkDatabase>().dao }
}

val useCasesModule = module {
    single { GetAllNotesUseCase(get()) }
    single { GetNoteByIdUseCase(get()) }
    single { UpsertNoteUseCase(get()) }
    single { DeleteNoteUseCase(get()) }
    single { LogOutUseCase(get(), get()) }
}

val syncWorkModule = module {

    single { WorkManager.getInstance(androidContext()) }

    single { SyncRequest(get()) }

    factory{ (context: Context, params: WorkerParameters) ->

        SyncWorker(context = context, workerParams = params, repository =  get())
    }

}

