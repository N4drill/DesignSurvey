package com.example.androidsampleconfiguration.app.domain

import android.content.Context
import android.content.SharedPreferences
import com.example.androidsampleconfiguration.app.di.AppContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
    @AppContext private val context: Context
) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun deleteUserId() {
        with(sharedPreferences.edit()) {
            putString(USER_ID_PREF, null)
            commit()
        }
    }

    fun saveUserId(userId: String) {
        with(sharedPreferences.edit()) {
            putString(USER_ID_PREF, userId)
            commit()
        }
    }

    fun getUserId(): String? = sharedPreferences.getString(USER_ID_PREF, null)

    companion object {
        private const val PREF_FILE_NAME = "APP_PREFERENCES"
        private const val USER_ID_PREF = "USER_ID"
    }
}
