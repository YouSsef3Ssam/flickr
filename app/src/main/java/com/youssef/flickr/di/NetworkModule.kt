package com.youssef.flickr.di

import android.app.Application
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.youssef.flickr.BuildConfig
import com.youssef.flickr.framework.utils.Constants.Network.BASE_URL
import com.youssef.flickr.framework.utils.Constants.Network.Queries
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private val baseUrl: String
        get() = BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(
        headerInterceptor: Interceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        val httpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(chuckerInterceptor)
        }
        httpClientBuilder.addInterceptor(headerInterceptor)

        return httpClientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideChuckInterceptor(application: Application): ChuckerInterceptor =
        ChuckerInterceptor.Builder(application.applicationContext).build()

    @Provides
    @Singleton
    fun provideHeaderInterceptor() =
        Interceptor { chain ->
            val originalRequest = chain.request()
            val url: HttpUrl = originalRequest.url.newBuilder()
                .addQueryParameter(Queries.API_KEY, BuildConfig.API_KEY)
                .addQueryParameter(Queries.FORMAT_KEY, Queries.FORMAT_VALUE)
                .addQueryParameter(Queries.NO_JSON_CALLBACK_KEY, Queries.NO_JSON_CALLBACK_VALUE)
                .build()
            val newRequest = originalRequest.newBuilder().url(url).build()
            chain.proceed(newRequest)
        }
}
