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

import io.rekast.sdk.BuildConfig
import io.rekast.sdk.model.AccountBalance
import io.rekast.sdk.model.AccountHolder
import io.rekast.sdk.model.BasicUserInfo
import io.rekast.sdk.model.MomoNotification
import io.rekast.sdk.model.MomoTransaction
import io.rekast.sdk.model.ProviderCallBackHost
import io.rekast.sdk.model.UserInfoWithConsent
import io.rekast.sdk.model.authentication.AccessToken
import io.rekast.sdk.model.authentication.ApiKey
import io.rekast.sdk.model.authentication.ApiUser
import io.rekast.sdk.model.authentication.credentials.AccessTokenCredentials
import io.rekast.sdk.model.authentication.credentials.BasicAuthCredentials
import io.rekast.sdk.network.service.products.CollectionService
import io.rekast.sdk.network.service.products.DisbursementsService
import io.rekast.sdk.repository.data.DataResponse
import io.rekast.sdk.repository.data.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Response

/**
 * The DefaultRepository class is responsible for making network calls to the MTN MOMO APIs.
 *
 * This class holds all the common API methods for various MTN MOMO products, including
 * user management, transaction processing, and status checks.
 *
 * @property authenticationService The service for handling authentication-related API calls.
 * @property commonService The service for common API calls.
 * @property disbursementsService The service for disbursement-related API calls.
 * @property collection The service for collection-related API calls.
 * @property basicAuthCredentialsT The credentials for basic authentication.
 * @property accessTokenCredentialsT The credentials for access token authentication.
 */
@Singleton
class DefaultRepository @Inject constructor(
    private val defaultSource: DefaultSource,
    private val disbursementsService: DisbursementsService,
    private val collection: CollectionService,
    private val basicAuthCredentialsT: BasicAuthCredentials,
    private val accessTokenCredentialsT: AccessTokenCredentials
) : DataResponse() {

    /**
     * Sets up basic authentication credentials.
     *
     * @param basicAuthCredentials The basic authentication credentials to set.
     */
    fun setUpBasicAuth(basicAuthCredentials: BasicAuthCredentials) {
        basicAuthCredentialsT.apiUserId = basicAuthCredentials.apiUserId
        basicAuthCredentialsT.apiKey = basicAuthCredentials.apiKey
    }

    /**
     * Sets up access token authentication credentials.
     *
     * @param accessTokenCredentials The access token credentials to set.
     */
    fun setUpAccessTokenAuth(accessTokenCredentials: AccessTokenCredentials) {
        accessTokenCredentialsT.accessToken = accessTokenCredentials.accessToken
    }

    fun getBasicAuth(): BasicAuthCredentials {
        return basicAuthCredentialsT
    }

    fun getAccessTokenAuth(): AccessTokenCredentials {
        return accessTokenCredentialsT
    }

    /**
     * Creates a new API user.
     *
     * @param apiVersion The version of the API to use.
     * @param uuid A unique identifier for the request.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the created [Unit].
     */
    fun createApiUser(
        providerCallBackHost: ProviderCallBackHost,
        apiVersion: String,
        uuid: String,
        productSubscriptionKey: String
    ): Flow<NetworkResult<ApiUser>> {
        return flow<NetworkResult<ApiUser>> {
            emit(safeApiCall { defaultSource.createApiUser(providerCallBackHost = providerCallBackHost, apiVersion = apiVersion, uuid = uuid, productSubscriptionKey = productSubscriptionKey) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Checks whether the supplied API user exists.
     *
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the [ApiUser] if found.
     */
    fun checkApiUser(
        apiVersion: String,
        productSubscriptionKey: String
    ): Flow<NetworkResult<ApiUser>> {
        return flow<NetworkResult<ApiUser>> {
            emit(safeApiCall { defaultSource.getApiUser(apiVersion, userId = BuildConfig.MOMO_API_USER_ID, productSubscriptionKey = productSubscriptionKey) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Gets the API Key based on the ApiUser Id and OCP Subscription Id.
     *
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @return A [Response] containing the created [ApiKey].
     */
    fun createApiKey(
        apiVersion: String,
        productSubscriptionKey: String
    ): Flow<NetworkResult<ApiKey>> {
        return flow<NetworkResult<ApiKey>> {
            emit(safeApiCall { defaultSource.createApiKey(apiVersion = apiVersion, userId = BuildConfig.MOMO_API_USER_ID, productSubscriptionKey = productSubscriptionKey) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Gets the Access Token based on the ApiUser ID, OCP Subscription Id, and the API Key.
     *
     * @param productSubscriptionKey The subscription key for the product.
     * @param productType The type of product for which to obtain the access token.
     * @return A [Response] containing the obtained [AccessToken].
     */
    fun getAccessToken(
        productSubscriptionKey: String,
        productType: String
    ): Flow<NetworkResult<AccessToken>> {
        return flow<NetworkResult<AccessToken>> {
            emit(safeApiCall { defaultSource.getAccessToken(productType = productType, productSubscriptionKey = productSubscriptionKey) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Gets the account balance of the entity/user initiating the transaction.
     *
     * @param currency The currency for which to get the account balance.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for which to get the account balance.
     * @return A [Response] containing the [AccountBalance].
     */
    fun getAccountBalance(
        productType: String,
        apiVersion: String,
        currency: String,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<AccountBalance>> {
        return flow<NetworkResult<AccountBalance>> {
            emit(
                safeApiCall {
                    if (StringUtils.isNotBlank(currency)) {
                        defaultSource.getAccountBalanceInSpecificCurrency(productType = productType, apiVersion = apiVersion, currency = currency, productSubscriptionKey = productSubscriptionKey, environment = environment)
                    } else {
                        defaultSource.getAccountBalance(productType = productType, apiVersion = apiVersion, productSubscriptionKey = productSubscriptionKey, environment = environment)
                    }
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Retrieves the basic user information for a specified MTN MOMO user.
     *
     * @param accountHolder The identifier for the account holder.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for which to retrieve the user information.
     * @return A [Response] containing the [BasicUserInfo] of the specified user.
     */
    fun getBasicUserInfo(
        productType: String,
        apiVersion: String,
        accountHolder: String,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<BasicUserInfo>> {
        return flow<NetworkResult<BasicUserInfo>> {
            emit(safeApiCall { defaultSource.getBasicUserInfo(productType = productType, apiVersion = apiVersion, accountHolder = accountHolder, productSubscriptionKey = productSubscriptionKey, environment = environment) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Retrieves the user information for a specified MTN MOMO user with consent.
     *
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for which to retrieve the user information.
     * @return A [Response] containing the [UserInfoWithConsent] of the specified user.
     */
    suspend fun getUserInfoWithConsent(
        productType: String,
        apiVersion: String,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<UserInfoWithConsent>> {
        return flow<NetworkResult<UserInfoWithConsent>> {
            emit(safeApiCall { defaultSource.getUserInfoWithConsent(productType = productType, apiVersion = apiVersion, productSubscriptionKey = productSubscriptionKey, environment = environment) })
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Sends a request to transfer funds to a specified account.
     *
     * @param accessToken The access token for authentication.
     * @param momoTransaction The transaction details for the transfer.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for the transfer.
     * @param productSubscriptionKey The subscription key for the product.
     * @param uuid A unique identifier for the request.
     * @return A [Response] indicating the result of the transfer request.
     */
    suspend fun transfer(
        productType: String,
        apiVersion: String,
        momoTransaction: MomoTransaction,
        uuid: String,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<Unit>> {
        return flow<NetworkResult<Unit>> {
            emit(
                safeApiCall {
                    defaultSource.transfer(productType = productType, apiVersion = apiVersion, momoTransaction = momoTransaction, uuid = uuid, productSubscriptionKey = productSubscriptionKey, environment = environment)
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Retrieves the status of a transfer based on the provided transfer ID.
     *
     * @param referenceId The reference ID of the transfer.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for the transfer.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] containing the status of the transfer.
     */
    suspend fun getTransferStatus(
        productType: String,
        apiVersion: String,
        referenceId: String,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<ResponseBody>> {
        return flow<NetworkResult<ResponseBody>> {
            emit(
                safeApiCall {
                    defaultSource.getTransferStatus(productType = productType, apiVersion = apiVersion, referenceId = referenceId, productSubscriptionKey = productSubscriptionKey, environment = environment)
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Sends a request to pay a user, identified by the provided reference ID.
     *
     * @param momoNotification The notification details for the payment.
     * @param referenceId The reference ID of the user to pay.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for the payment.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] indicating the result of the payment request.
     */
    suspend fun requestToPayDeliveryNotification(
        productType: String,
        apiVersion: String,
        referenceId: String,
        momoNotification: MomoNotification,
        productSubscriptionKey: String,
        environment: String
    ): Flow<NetworkResult<ResponseBody>> {
        return flow<NetworkResult<ResponseBody>> {
            emit(
                safeApiCall {
                    defaultSource.requestToPayDeliveryNotification(
                        productType = productType,
                        apiVersion = apiVersion,
                        referenceId = referenceId,
                        momoNotification = momoNotification,
                        productSubscriptionKey = productSubscriptionKey,
                        environment = environment
                    )
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    /**
     * Validates the status of an account holder.
     *
     * @param accountHolder The account holder details to validate.
     * @param apiVersion The version of the API to use.
     * @param productType The type of product for the validation.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] indicating the result of the account holder status validation.
     */
    suspend fun validateAccountHolderStatus(productType: String, apiVersion: String, accountHolder: AccountHolder, productSubscriptionKey: String, environment: String): Response<ResponseBody> {
        return defaultSource.validateAccountHolderStatus(productType, apiVersion, accountHolder, productSubscriptionKey, environment)
    }

    /**
     * Requests a payment to be processed.
     *
     * @param accessToken The access token for authentication.
     * @param momoTransaction The transaction details.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param uuid A unique identifier for the request.
     * @return A [Response] indicating the result of the request.
     */
    suspend fun requestToPay(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return collection.requestToPay(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    /**
     * Requests the status of a payment transaction.
     *
     * @param referenceId The reference ID of the transaction.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] containing the status of the transaction.
     */
    suspend fun requestToPayTransactionStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return collection.requestToPayTransactionStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Requests a withdrawal to be processed.
     *
     * @param accessToken The access token for authentication.
     * @param momoTransaction The transaction details.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param uuid A unique identifier for the request.
     * @return A [Response] indicating the result of the request.
     */
    suspend fun requestToWithdraw(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return collection.requestToWithdraw(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    /**
     * Requests the status of a withdrawal transaction.
     *
     * @param referenceId The reference ID of the transaction.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] containing the status of the transaction.
     */
    suspend fun requestToWithdrawTransactionStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return collection.requestToWithdrawTransactionStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Requests a deposit to be processed.
     *
     * @param accessToken The access token for authentication.
     * @param momoTransaction The transaction details.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param uuid A unique identifier for the request.
     * @return A [Response] indicating the result of the request.
     */
    suspend fun deposit(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return disbursementsService.deposit(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    /**
     * Gets the status of a deposit transaction.
     *
     * @param referenceId The reference ID of the transaction.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] containing the status of the deposit transaction.
     */
    suspend fun getDepositStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return disbursementsService.getDepositStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Requests a refund to be processed.
     *
     * @param accessToken The access token for authentication.
     * @param momoTransaction The transaction details.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param uuid A unique identifier for the request.
     * @return A [Response] indicating the result of the request.
     */
    suspend fun refund(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return disbursementsService.refund(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    /**
     * Gets the status of a refund transaction.
     *
     * @param referenceId The reference ID of the transaction.
     * @param apiVersion The version of the API to use.
     * @param productSubscriptionKey The subscription key for the product.
     * @param accessToken The access token for authentication.
     * @return A [Response] containing the status of the refund transaction.
     */
    suspend fun getRefundStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return disbursementsService.getRefundStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }
}
