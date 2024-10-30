/*
 * Copyright 2023, Benjamin Mwalimu Mulyungi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rekast.sdk.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rekast.sdk.BuildConfig
import io.rekast.sdk.network.Interceptor.UnsafeOkHttpClient
import io.rekast.sdk.network.service.AuthenticationService
import io.rekast.sdk.network.service.products.CommonService
import io.rekast.sdk.network.service.products.DisbursementsService
import io.rekast.sdk.network.service.products.CollectionService
import io.rekast.sdk.utils.Settings
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Provides an instance of retrofit to all classes that need it.
 */

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun getAuthentication(retrofit: Retrofit): AuthenticationService =
        retrofit.create(AuthenticationService::class.java)

    @Provides
    @Singleton
    fun getMomoCollection(retrofit: Retrofit): CollectionService =
        retrofit.create(CollectionService::class.java)

    @Provides
    @Singleton
    fun getCommon(retrofit: Retrofit): CommonService =
        retrofit.create(CommonService::class.java)

    @Provides
    @Singleton
    fun getDisbursement(retrofit: Retrofit): DisbursementsService =
        retrofit.create(DisbursementsService::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        baseUrl: String,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun providesBaseUrl(): String {
        return BuildConfig.MOMO_BASE_URL
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        baseUrl: String
    ): OkHttpClient {
        val builder = if (baseUrl.contains("https")) {
            OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        } else {
            UnsafeOkHttpClient().unsafeOkHttpClient.addInterceptor(httpLoggingInterceptor)
        }

        return builder.connectTimeout(Settings().CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Settings().WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Settings().READ_TIMEOUT, TimeUnit.SECONDS).build()
    }
}
