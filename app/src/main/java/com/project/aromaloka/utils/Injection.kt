package com.project.aromaloka.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.project.aromaloka.models.Repository
import com.project.aromaloka.service.ApiConfig

private val Context.ds: DataStore<Preferences> by preferencesDataStore("token")

class Injection {
    companion object {
        fun provideRepository(context: Context): Repository {
            val preferences = SessionPreferences.getInstance(context.ds)
            val service = ApiConfig.getApiService()
            val mlService = ApiConfig.getApiMLService()
            return Repository.getInstance(preferences, service, mlService)
        }
    }


}