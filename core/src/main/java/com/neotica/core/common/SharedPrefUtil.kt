package com.neotica.core.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class SharedPrefUtil(private val dataStore: DataStore<Preferences>) {
    private object Keys {
        val TOKEN = stringPreferencesKey("token")
    }

    private var _token: MutableLiveData<String?> = MutableLiveData()
    val token: LiveData<String?> = _token

    suspend fun setToken(token: String) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[Keys.TOKEN] = token
            }
        }
    }

    suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.clear()
            }
        }
    }

    suspend fun loadToken() {
        val tokenValue = dataStore.data.map { preferences ->
            preferences[Keys.TOKEN]
        }.first()
        _token.value = tokenValue
    }
}