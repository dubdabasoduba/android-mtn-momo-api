/*
 * Copyright 2023-2024, Benjamin Mwalimu
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
package io.rekast.sdk.repository

import io.rekast.sdk.model.ProviderCallBackHost
import io.rekast.sdk.network.service.AuthenticationService
import javax.inject.Inject

/**
 * DefaultSource is responsible for handling API calls related to user management
 * and authentication in the MTN MOMO SDK.
 *
 * This class acts as a bridge between the repository and the authentication service,
 * providing methods to create API users, retrieve user details, create API keys,
 * and obtain access tokens.
 *
 * @property authenticationService The service for handling authentication-related API calls.
 */
class DefaultSource @Inject constructor(private val authenticationService: AuthenticationService) {

    /**
     * Creates a new API user.
     *
     * @param providerCallBackHost The callback host for the provider.
     * @param apiVersion The version of the API to use.
     * @param uuid A unique identifier for the request.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the created [ApiUser].
     */
    suspend fun createApiUser(
        providerCallBackHost: ProviderCallBackHost,
        apiVersion: String,
        uuid: String,
        productSubscriptionKey: String
    ) = authenticationService.createApiUser(providerCallBackHost = providerCallBackHost, apiVersion = apiVersion, uuid = uuid, productSubscriptionKey = productSubscriptionKey)

    /**
     * Retrieves the details of an existing API user.
     *
     * @param apiVersion The version of the API to use.
     * @param userId The ID of the API user to retrieve.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the requested [ApiUser].
     */
    suspend fun getApiUser(apiVersion: String, userId: String, productSubscriptionKey: String) = authenticationService.getApiUser(apiVersion = apiVersion, apiUser = userId, productSubscriptionKey = productSubscriptionKey)

    /**
     * Creates a new API key for the specified API user.
     *
     * @param apiVersion The version of the API to use.
     * @param userId The ID of the API user for whom to create the key.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the generated [ApiKey].
     */
    suspend fun createApiKey(apiVersion: String, userId: String, productSubscriptionKey: String) = authenticationService.createApiKey(apiVersion = apiVersion, apiUser = userId, productSubscriptionKey = productSubscriptionKey)

    /**
     * Obtains an access token for the specified product type.
     *
     * @param productType The type of product for which to obtain the access token.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the obtained [AccessToken].
     */
    suspend fun getAccessToken(productType: String, productSubscriptionKey: String) = authenticationService.getAccessToken(productType = productType, productSubscriptionKey = productSubscriptionKey)
}
