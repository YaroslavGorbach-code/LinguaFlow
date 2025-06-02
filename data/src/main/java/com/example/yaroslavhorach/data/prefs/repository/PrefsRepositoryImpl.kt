package com.example.yaroslavhorach.data.prefs.repository

import com.example.yaroslavhorach.datastore.LinguaPrefsDataSource
import com.example.yaroslavhorach.datastore.model.asDomainModel
import com.example.yaroslavhorach.domain.prefs.PrefsRepository
import com.example.yaroslavhorach.domain.prefs.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PrefsRepositoryImpl @Inject constructor(
    private val prefsDataSource: LinguaPrefsDataSource
) : PrefsRepository {
    override fun getUserData(): Flow<UserData> {
        return prefsDataSource.userData.map { it.asDomainModel() }
    }

    override suspend fun useToken() {
        prefsDataSource.useToken()
    }

    override suspend fun refreshTokens() {
        prefsDataSource.refreshTokens()
    }
}