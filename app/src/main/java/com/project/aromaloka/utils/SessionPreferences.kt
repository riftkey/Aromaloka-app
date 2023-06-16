package com.project.aromaloka.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.project.aromaloka.models.ResponseSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences


class SessionPreferences private constructor(
    private val ds: DataStore<Preferences>
) {


    fun getSession(): Flow<ResponseSession?> {
        return ds.data.map { preferences ->
            ResponseSession(
                preferences[KEY_NAME] ?: "",
                preferences[KEY_TOKEN]?.trim() ?: "",
                preferences[KEY_STATE] ?: false
            )
        }
    }


    suspend fun saveSession(ss: ResponseSession) {
        ds.edit { preferences ->
            preferences[KEY_NAME] = ss.name
            preferences[KEY_TOKEN] = ss.token
            preferences[KEY_STATE] = ss.isLogin
        }
    }

    suspend fun login() {
        ds.edit { preferences ->
            preferences[KEY_STATE] = true
        }
    }

    suspend fun logout() {
        ds.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null
        private val KEY_NAME = stringPreferencesKey("name")
        private val KEY_TOKEN = stringPreferencesKey("token")
        private val KEY_STATE = booleanPreferencesKey("state")

        fun getInstance(ds: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPreferences(ds)
                INSTANCE = instance
                instance
            }
        }
    }
}