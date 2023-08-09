package com.pitjarus.pitjarusandroidtest.data.remote

import com.pitjarus.pitjarusandroidtest.data.model.StoreResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("login/loginTest")
    @Headers("Accept:application/json", "Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<StoreResponse>
}