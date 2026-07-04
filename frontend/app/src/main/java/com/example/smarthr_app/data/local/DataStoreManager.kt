package com.example.smarthr_app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smarthr_app.data.model.User
import com.example.smarthr_app.data.model.ExtraProfileDetails
import com.example.smarthr_app.data.model.OnDutyRecord
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_prefs")
        private val USER_KEY = stringPreferencesKey("user")
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val PENDING_COMPANY_CODE_KEY = stringPreferencesKey("pending_company_code")
        private val EXTRA_PROFILE_KEY = stringPreferencesKey("extra_profile_details")
        private val ON_DUTY_HISTORY_KEY = stringPreferencesKey("on_duty_history")
    }

    private val gson = Gson()

    suspend fun saveUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[USER_KEY] = gson.toJson(user)
            preferences[IS_LOGGED_IN_KEY] = true
        }
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun savePendingCompanyCode(companyCode: String?) {
        context.dataStore.edit { preferences ->
            if (companyCode != null) {
                preferences[PENDING_COMPANY_CODE_KEY] = companyCode
            } else {
                preferences.remove(PENDING_COMPANY_CODE_KEY)
            }
        }
    }

    suspend fun saveExtraProfileDetails(details: ExtraProfileDetails) {
        context.dataStore.edit { preferences ->
            preferences[EXTRA_PROFILE_KEY] = gson.toJson(details)
        }
    }

    val user: Flow<User?> = context.dataStore.data.map { preferences ->
        val userJson = preferences[USER_KEY]
        if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else null
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN_KEY] ?: false
    }

    val token: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    val pendingCompanyCode: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PENDING_COMPANY_CODE_KEY]
    }

    val extraProfileDetails: Flow<ExtraProfileDetails?> = context.dataStore.data.map { preferences ->
        val json = preferences[EXTRA_PROFILE_KEY]
        if (json != null) {
            gson.fromJson(json, ExtraProfileDetails::class.java)
        } else null
    }

    val onDutyHistory: Flow<List<OnDutyRecord>> = context.dataStore.data.map { preferences ->
        val json = preferences[ON_DUTY_HISTORY_KEY]
        if (json != null) {
            try {
                val type = object : com.google.gson.reflect.TypeToken<List<OnDutyRecord>>() {}.type
                gson.fromJson(json, type) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else emptyList()
    }

    suspend fun saveOnDutyRecord(record: OnDutyRecord) {
        context.dataStore.edit { preferences ->
            val json = preferences[ON_DUTY_HISTORY_KEY]
            val list = if (json != null) {
                try {
                    val type = object : com.google.gson.reflect.TypeToken<List<OnDutyRecord>>() {}.type
                    gson.fromJson<List<OnDutyRecord>>(json, type).toMutableList()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }
            list.add(0, record)
            preferences[ON_DUTY_HISTORY_KEY] = gson.toJson(list)
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}