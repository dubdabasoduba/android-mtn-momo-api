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
package io.rekast.sdk.repository

import io.rekast.sdk.BuildConfig
import io.rekast.sdk.model.api.AccountBalance
import io.rekast.sdk.model.api.AccountHolder
import io.rekast.sdk.model.api.BasicUserInfo
import io.rekast.sdk.model.api.MomoNotification
import io.rekast.sdk.model.api.MomoTransaction
import io.rekast.sdk.model.api.UserInfoWithConsent
import io.rekast.sdk.model.authentication.AccessToken
import io.rekast.sdk.model.authentication.User
import io.rekast.sdk.model.authentication.UserKey
import io.rekast.sdk.network.service.AuthenticationService
import io.rekast.sdk.network.service.products.CollectionService
import io.rekast.sdk.network.service.products.CommonService
import io.rekast.sdk.network.service.products.DisbursementsService
import javax.inject.Inject
import javax.inject.Singleton
import okhttp3.ResponseBody
import org.apache.commons.lang3.StringUtils
import retrofit2.Response

/**
 * The BaseRepository Class. Responsible for making the actual network call to the MOMO APIs.
 * This calls holds all the commons API methods for all the MTN MOMO Products
 */
@Singleton
class DefaultRepository @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val commonService: CommonService,
    private val disbursementsService: DisbursementsService,
    private val collection: CollectionService
) {

    suspend fun createApiUser(
        productSubscriptionKey: String,
        apiVersion: String,
        uuid: String
    ): Response<User> {
        return authenticationService.createApiUser(apiVersion, uuid, productSubscriptionKey)
    }

    /**
     * Check whether the supplied user exists
     */
    suspend fun checkApiUser(
        productSubscriptionKey: String,
        apiVersion: String
    ): Response<User> {
        return authenticationService
            .getApiUser(apiVersion, BuildConfig.MOMO_API_USER_ID, productSubscriptionKey)
    }

    /**
     * Get the API Key based on the User Id and OCP Subscription Id
     */
    fun getUserApiKey(
        productSubscriptionKey: String,
        apiVersion: String
    ): Response<UserKey> {
        return authenticationService
            .getApiUserKey(apiVersion, BuildConfig.MOMO_API_USER_ID, productSubscriptionKey)
    }

    /**
     * Get the Access Token based on the User ID, OCP Subscription Id and the API Key
     */
    fun getAccessToken(
        productSubscriptionKey: String,
        apiKey: String,
        productType: String
    ): Response<AccessToken> {
        return authenticationService.getAccessToken(productType, productSubscriptionKey)
    }

    /**
     * Gets the account balance of the entity/user initiating the transaction.
     */
    fun getAccountBalance(
        currency: String?,
        productSubscriptionKey: String,
        accessToken: String,
        apiVersion: String,
        productType: String
    ): Response<AccountBalance> {
        return if (StringUtils.isNotBlank(currency)) {
            commonService.getAccountBalanceInSpecificCurrency(
                currency.toString(),
                productType,
                apiVersion,
                productSubscriptionKey,
                BuildConfig.MOMO_ENVIRONMENT
            )
        } else {
            commonService.getAccountBalance(productType, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
        }
    }

    /**
     * Get the basic user information for a certain MTN MOMO User
     */
    fun getBasicUserInfo(
        accountHolder: String,
        productSubscriptionKey: String,
        accessToken: String,
        apiVersion: String,
        productType: String
    ): Response<BasicUserInfo> {
        return commonService.getBasicUserInfo(accountHolder, productType, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Get the user information for a certain MTN MOMO User with consent
     */
    fun getUserInfoWithConsent(
        productSubscriptionKey: String,
        accessToken: String,
        apiVersion: String,
        productType: String
    ): Response<UserInfoWithConsent> {
        return commonService.getUserInfoWithConsent(productType, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Sends a request to transfer to an account
     */
    fun transfer(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productType: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return commonService.transfer(momoTransaction, apiVersion, productType, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    /**
     * Get the transfer status based on the transfer Id [referenceId]
     */
    fun getTransferStatus(
        referenceId: String,
        apiVersion: String,
        productType: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return commonService.getTransferStatus(referenceId, apiVersion, productType, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    /**
     * Sends a request to pay a user. The user is identified by the [referenceId]
     */
    fun requestToPayDeliveryNotification(
        momoNotification: MomoNotification,
        referenceId: String,
        apiVersion: String,
        productType: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return commonService.requestToPayDeliveryNotification(
            momoNotification,
            referenceId,
            apiVersion,
            productType,
            productSubscriptionKey,
            BuildConfig.MOMO_ENVIRONMENT,
            momoNotification.notificationMessage
        )
    }

    /**
     * Validate an account status.
     */
    fun validateAccountHolderStatus(
        accountHolder: AccountHolder,
        apiVersion: String,
        productType: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return commonService.validateAccountHolderStatus(
            accountHolder.partyId,
            accountHolder.partyIdType,
            apiVersion,
            productType,
            productSubscriptionKey,
            BuildConfig.MOMO_ENVIRONMENT
        )
    }

    fun requestToPay(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return collection.requestToPay(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    fun requestToPayTransactionStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return collection.requestToPayTransactionStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    fun requestToWithdraw(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return collection.requestToWithdraw(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    fun requestToWithdrawTransactionStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return collection.requestToWithdrawTransactionStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    fun deposit(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return disbursementsService.deposit(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    fun getDepositStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return disbursementsService.getDepositStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }

    fun refund(
        accessToken: String,
        momoTransaction: MomoTransaction,
        apiVersion: String,
        productSubscriptionKey: String,
        uuid: String
    ): Response<Unit> {
        return disbursementsService.refund(momoTransaction, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT, uuid)
    }

    fun getRefundStatus(
        referenceId: String,
        apiVersion: String,
        productSubscriptionKey: String,
        accessToken: String
    ): Response<ResponseBody> {
        return disbursementsService.getRefundStatus(referenceId, apiVersion, productSubscriptionKey, BuildConfig.MOMO_ENVIRONMENT)
    }
}
