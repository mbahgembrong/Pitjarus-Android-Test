package com.pitjarus.pitjarusandroidtest.di


import com.pitjarus.pitjarusandroidtest.BuildConfig
import com.pitjarus.pitjarusandroidtest.data.remote.AuthApi
import com.pitjarus.pitjarusandroidtest.data.remote.AuthRemoteDataSource
import com.pitjarus.pitjarusandroidtest.utils.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor{
        val interceptor = HttpLoggingInterceptor(object :HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Timber.i(message)
            }
        })
        return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    }


    @Provides
    @Singleton
    fun provideRetrofit( client: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
//        gsonConverterFactory: GsonConverterFactory
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor):OkHttpClient   =
        if (BuildConfig.DEBUG) {
             OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }

    @Provides
    fun provideAuthService(retrofit: Retrofit):AuthApi= retrofit.create(AuthApi::class.java)


    @Provides
    @Singleton
    fun provideDataSource(authApi: AuthApi)=AuthRemoteDataSource(authApi)

}