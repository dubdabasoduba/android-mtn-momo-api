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
package io.rekast.sdk.network.api.client

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rekast.sdk.BuildConfig
import io.rekast.sdk.network.okhttp.UnsafeOkHttpClient
import io.rekast.sdk.network.service.Authentication
import io.rekast.sdk.network.service.products.Common
import io.rekast.sdk.network.service.products.Disbursements
import io.rekast.sdk.network.service.products.MomoCollection
import io.rekast.sdk.utils.Settings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Provides an instance of retrofit to all classes that need it.
 */

@Module
@InstallIn(SingletonComponent::class)
object Clients {
    /**
     * Common APIs across all the Products
     */
    @Provides
    @Singleton
    fun getAuthentication(baseUrl: String, authentication: Interceptor?): Authentication =
        provideRetrofit(baseUrl, authentication).create(Authentication::class.java)

    @Provides
    @Singleton
    fun getMomoCollection(baseUrl: String, authentication: Interceptor): MomoCollection =
        provideRetrofit(baseUrl, authentication).create(MomoCollection::class.java)

    @Provides
    @Singleton
    fun getCommon(baseUrl: String, authentication: Interceptor): Common =
        provideRetrofit(baseUrl, authentication).create(Common::class.java)

    @Provides
    @Singleton
    fun getDisbursement(baseUrl: String, authentication: Interceptor): Disbursements =
        provideRetrofit(baseUrl, authentication).create(Disbursements::class.java)

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, authentication: Interceptor?): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val builder = if (baseUrl == BuildConfig.MOMO_BASE_URL) {
            UnsafeOkHttpClient().unsafeOkHttpClient.addInterceptor(httpLoggingInterceptor)
        } else {
            OkHttpClient.Builder()
        }
        val client = returnOkHttpClient(builder, authentication)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private fun returnOkHttpClient(
        builder: OkHttpClient.Builder,
        authentication: Interceptor?
    ): OkHttpClient {
        return if (authentication == null) {
            builder.connectTimeout(Settings().CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Settings().WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Settings().READ_TIMEOUT, TimeUnit.SECONDS)
                .build()
        } else {
            builder.connectTimeout(Settings().CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Settings().WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Settings().READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(authentication)
                .build()
        }
    }
}
