package com.tonyxlab.notemark.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tonyxlab.notemark.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.KoinApplication.Companion.init


val Context.dataStore by preferencesDataStore(Constants.DATASTORE_PREF_NAME)
object TokenStorage {

    lateinit var dataStore: DataStore<Preferences>
    private val ACCESS_TOKEN = stringPreferencesKey(Constants.ACCESS_TOKEN)
    private val REFRESH_TOKEN = stringPreferencesKey(Constants.ACCESS_TOKEN)

    fun init(context:Context) {

        dataStore = context.dataStore

    }

   suspend fun saveTokens (accessToken: String, refreshToken: String){

        dataStore.edit { prefs ->

          prefs[ACCESS_TOKEN] = accessToken
          prefs[REFRESH_TOKEN] = refreshToken

        }
    }


    suspend fun getAccessToken(): String? {

        return dataStore.data.first()[ACCESS_TOKEN]
    }

    suspend fun getRefreshToken(): String? {

        return dataStore.data.first()[REFRESH_TOKEN]
    }

    suspend fun clearTokens() {

        dataStore.edit { prefs -> prefs.clear() }
    }


}
