package com.example.yaroslavhorach.datastore

import androidx.datastore.core.DataStore
import com.example.yaroslavhorach.common.utill.isToday
import com.example.yaroslavhorach.datastore.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class LinguaPrefsDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData: Flow<UserData> = userPreferences.data
        .map {
            UserData(
                availableTokens = it.availableTokens,
                maxTokens = 3
            )
        }

    suspend fun refreshTokens() {
        userPreferences.updateData { prefs ->
            if (prefs.lastTimeTokenWasUsed.isToday().not()) {
                prefs.toBuilder()
                    .setAvailableTokens(3)
                    .build()
            } else {
                prefs
            }
        }
    }

    suspend fun useToken() {
        userPreferences.updateData { prefs ->
            if (prefs.availableTokens > 0) {
                prefs.toBuilder()
                    .setLastTimeTokenWasUsed(Date().time)
                    .setAvailableTokens(prefs.availableTokens.dec())
                    .build()
            } else {
                prefs
            }
        }
    }
}