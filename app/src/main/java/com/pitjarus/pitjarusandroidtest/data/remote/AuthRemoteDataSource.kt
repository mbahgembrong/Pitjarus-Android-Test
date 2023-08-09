package com.pitjarus.pitjarusandroidtest.data.remote

import javax.inject.Inject

class AuthRemoteDataSource@Inject constructor(
    private val authApi: AuthApi
): BaseDataSource() {

    suspend fun login(username:String,password:String) = getResult { authApi.login(username,password)}

}