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
package io.rekast.sdk.network.interceptor

import io.rekast.sdk.model.authentication.AccessTokenCredentials
import io.rekast.sdk.utils.MomoConstants
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

/**
 * This is an authentication Interceptor. It is used after the client has received and Access Token.
 * It adds access token  to all endpoints that need Access Token AuthenticationService.
 * @param [accessToken]
 */
class AccessToken(
    private var accessTokenCredentials: AccessTokenCredentials
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = accessTokenCredentials.accessToken
        val request = chain.request().newBuilder()
            .addHeader(MomoConstants.Headers.AUTHORIZATION, "${MomoConstants.TokenTypes.BEARER} $accessToken")
            .build()

        return chain.proceed(request)
    }
}
