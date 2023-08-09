package com.pitjarus.pitjarusandroidtest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.pitjarus.pitjarusandroidtest.data.local.AppDatabase
import com.pitjarus.pitjarusandroidtest.data.model.Preference
import com.pitjarus.pitjarusandroidtest.data.model.asDatabase
import com.pitjarus.pitjarusandroidtest.data.remote.AuthRemoteDataSource
import com.pitjarus.pitjarusandroidtest.utils.Resource
import com.pitjarus.pitjarusandroidtest.utils.Status
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val appDatabase: AppDatabase,
) {
    val isLogin: LiveData<Boolean> = appDatabase.preferenceDao().getPreference("username").map { it?.value != null }
    fun login(username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val responseStatus = authRemoteDataSource.login(username, password)
        if (responseStatus.status == Status.SUCCESS) {
                if (!responseStatus.data!!.stores.isNullOrEmpty()) {
                    appDatabase.storeDao().insertAll(responseStatus.data!!.stores!!.map { it?.asDatabase()})
                }
                if (responseStatus.data!!.status == "success") {
                    appDatabase.preferenceDao().insertPref(Preference("username", username))
                }

            emit(Resource.success(responseStatus.data!!))
        } else if (responseStatus.status == Status.ERROR) {
            emit(Resource.error(responseStatus.message!!))
        }
    }

    fun logout() = liveData(Dispatchers.IO) {
        emit(Resource.loading())
        appDatabase.preferenceDao().deleteAll()
        emit(Resource.success(true))
    }

}