@file: RequiresApi(Build.VERSION_CODES.O)

package com.tonyxlab.notemark.di

import android.net.ConnectivityManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.work.WorkManager
import com.tonyxlab.notemark.data.json.JsonSerializerImpl
import com.tonyxlab.notemark.data.local.database.NoteMarkDatabase
import com.tonyxlab.notemark.data.local.database.dao.NoteDao
import com.tonyxlab.notemark.data.local.database.dao.SyncDao
import com.tonyxlab.notemark.data.local.datastore.DataStore
import com.tonyxlab.notemark.data.network.HttpClientFactory
import com.tonyxlab.notemark.data.remote.auth.AuthRepositoryImpl
import com.tonyxlab.notemark.data.remote.connectivity.ConnectivityObserverImpl
import com.tonyxlab.notemark.data.remote.sync.client.NotesRemote
import com.tonyxlab.notemark.data.remote.sync.client.NotesRemoteKtor
import com.tonyxlab.notemark.data.repository.NoteLocalWriter
import com.tonyxlab.notemark.data.repository.NoteRepositoryImpl
import com.tonyxlab.notemark.data.workmanager.SyncRequest
import com.tonyxlab.notemark.data.workmanager.SyncWorker
import com.tonyxlab.notemark.domain.auth.AuthRepository
import com.tonyxlab.notemark.domain.connectivity.ConnectivityObserver
import com.tonyxlab.notemark.domain.json.JsonSerializer
import com.tonyxlab.notemark.domain.repository.NoteRepository
import com.tonyxlab.notemark.domain.usecase.DeleteNoteUseCase
import com.tonyxlab.notemark.domain.usecase.GetAllNotesUseCase
import com.tonyxlab.notemark.domain.usecase.GetNoteByIdUseCase
import com.tonyxlab.notemark.domain.usecase.LogoutUseCase
import com.tonyxlab.notemark.domain.usecase.SyncQueueReaderUseCase
import com.tonyxlab.notemark.domain.usecase.UpsertNoteUseCase
import com.tonyxlab.notemark.presentation.screens.editor.EditorViewModel
import com.tonyxlab.notemark.presentation.screens.home.HomeViewModel
import com.tonyxlab.notemark.presentation.screens.landing.LandingViewModel
import com.tonyxlab.notemark.presentation.screens.login.LoginViewModel
import com.tonyxlab.notemark.presentation.screens.settings.SettingsViewModel
import com.tonyxlab.notemark.presentation.screens.signup.SignupViewModel
import com.tonyxlab.notemark.util.ApiEndpoints.BASE_URL
import com.tonyxlab.notemark.util.Constants
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.workerOf
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

val useCasesModule = module {
    single { GetAllNotesUseCase(get()) }
    single { GetNoteByIdUseCase(get()) }
    single { UpsertNoteUseCase(get()) }
    single { DeleteNoteUseCase(get()) }
    single { LogoutUseCase(get(), get(), get()) }
    single { SyncQueueReaderUseCase(get()) }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single {
        NoteLocalWriter(
                noteDao = get(),
                syncDao = get(),
                jsonSerializer = get(),
                dataStore = get()
        )
    }
    single<NoteRepository> { NoteRepositoryImpl(get(), get(),get()) }
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
    single<NoteDao> { get<NoteMarkDatabase>().noteDao }
    single<SyncDao> { get<NoteMarkDatabase>().syncDao }
}

val dataStoreModule = module {
    single { DataStore(androidContext()) }
}

val syncWorkModule = module {

    single { WorkManager.getInstance(androidContext()) }

    single { SyncRequest(get()) }



    workerOf(::SyncWorker)

}
val serializationModule = module {
    single<JsonSerializer> { JsonSerializerImpl() }
}

val networkModule = module {
    single {
        HttpClientFactory(get())
    }


    // Expose a singleton HttpClient built by the factory (so others can use it too)
    single<HttpClient> { get<HttpClientFactory>().provideMainHttpClient() }

    // Finally, bind NotesRemote (this is what SyncWorker needs)
    single<NotesRemote> {
        NotesRemoteKtor(
                client = get<HttpClient>(),
                baseUrl = BASE_URL,

                tokenProvider = { get<DataStore>().getAccessToken() }
        )
    }
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



