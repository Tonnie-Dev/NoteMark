package com.tonyxlab.notemark.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tonyxlab.notemark.domain.model.SyncInterval
import com.tonyxlab.notemark.domain.model.toPrefsInt
import com.tonyxlab.notemark.domain.model.toSyncInterval
import com.tonyxlab.notemark.util.Constants
import kotlinx.coroutines.flow.first


val Context.dataStore by preferencesDataStore(Constants.DATASTORE_PREF_NAME)

class DataStore(context: Context) {

    val dataStore = context.dataStore

    companion object{

        private val ACCESS_TOKEN = stringPreferencesKey(Constants.ACCESS_TOKEN)
        private val REFRESH_TOKEN = stringPreferencesKey(Constants.REFRESH_TOKEN)
        private val USERNAME = stringPreferencesKey(Constants.USERNAME_KEY)
        private val SYNC_INTERVAL = intPreferencesKey(Constants.SYNC_INTERVAL_KEY)
    }



    suspend fun saveTokens(accessToken: String, refreshToken: String, username: String) {

        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = accessToken
            prefs[REFRESH_TOKEN] = refreshToken
            prefs[USERNAME] = username
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

    suspend fun getUsername(): String? {

        return dataStore.data.first()[USERNAME]
    }

    suspend fun getSyncInterval(): SyncInterval {

        return dataStore.data.first()[SYNC_INTERVAL]?.toSyncInterval() ?: SyncInterval.MANUAL
    }

    suspend fun saveSyncInterval(interval: SyncInterval) {

        dataStore.edit { prefs -> prefs[SYNC_INTERVAL] = interval.toPrefsInt() }
    }

}
