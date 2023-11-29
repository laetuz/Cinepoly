package com.neotica.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.neotica.core.common.PreferenceDefaults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import com.neotica.core.domain.repo.PreferenceRepository

class PreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : PreferenceRepository {
    companion object {
        private val TOKEN = stringPreferencesKey("token")
    }

    override fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN] ?: PreferenceDefaults.TOKEN
        }
    }

    override suspend fun setToken(value: String) {
        dataStore.edit {
            it[TOKEN] = value
        }
    }

    override suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            dataStore.edit {
                it.clear()
            }
        }
    }
}