/*
 * Copyright 2023 - 2024, Benjamin Mwalimu
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
package io.rekast.sdk.sample.views

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rekast.sdk.BuildConfig
import io.rekast.sdk.model.MomoTransaction
import io.rekast.sdk.model.ProviderCallBackHost
import io.rekast.sdk.model.authentication.credentials.AccessTokenCredentials
import io.rekast.sdk.model.authentication.credentials.BasicAuthCredentials
import io.rekast.sdk.repository.DefaultRepository
import io.rekast.sdk.repository.data.NetworkResult
import io.rekast.sdk.sample.utils.SnackBarComponentConfiguration
import io.rekast.sdk.sample.utils.Utils
import io.rekast.sdk.utils.ProductType
import io.rekast.sdk.utils.Settings
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import timber.log.Timber

/**
 * ViewModel for managing authentication and API interactions.
 *
 * This ViewModel handles setting authentication credentials and making API calls.
 *
 * @property defaultRepository The repository for handling API calls related to MTN MOMO.
 * @property context The application context for accessing resources and utilities.
 * @property settings The settings utility for managing application settings.
 */
@HiltViewModel
open class AppMainViewModel @Inject constructor(
    private val defaultRepository: DefaultRepository,
    @ApplicationContext private val context: Context,
    private val settings: Settings
) : ViewModel() {

    private val _snackBarStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>()
    val snackBarStateFlow: SharedFlow<SnackBarComponentConfiguration> = _snackBarStateFlow.asSharedFlow()

    /**
     * Sets the Basic Authentication credentials.
     *
     * @param apiUserId The API user ID.
     * @param apiKey The API key.
     */
    fun setBasicAuth(apiUserId: String, apiKey: String) {
        val basicAuthCredentials = BasicAuthCredentials(apiUserId, apiKey)
        viewModelScope.launch {
            defaultRepository.setUpBasicAuth(basicAuthCredentials)
        }
    }

    /**
     * Sets the Access Token credentials.
     *
     * @param accessToken The access token.
     */
    fun setAccessToken(accessToken: String) {
        val accessTokenCredentials = AccessTokenCredentials(accessToken)
        viewModelScope.launch {
            defaultRepository.setUpAccessTokenAuth(accessTokenCredentials)
        }
    }

    /**
     * Checks the user status and creates an API user if necessary.
     *
     * This method checks if the API user exists and creates a new one if it does not.
     */
    fun checkUser() {
        val productType = settings.getProductSubscriptionKeys(ProductType.COLLECTION)
        viewModelScope.launch(Dispatchers.IO) {
            defaultRepository.checkApiUser(BuildConfig.MOMO_API_VERSION_V1, productType).collect { apiUser ->
                when (apiUser) {
                    is NetworkResult.Success -> { createApiKey() }
                    is NetworkResult.Error -> {
                        Timber.e(apiUser.message)
                        val providerCallBackHost = ProviderCallBackHost(providerCallbackHost = BuildConfig.MOMO_PROVIDER_CALBACK_HOST)
                        defaultRepository.createApiUser(providerCallBackHost, BuildConfig.MOMO_API_VERSION_V1, BuildConfig.MOMO_API_USER_ID, productType).collect { newApiUser ->
                            when (newApiUser) {
                                is NetworkResult.Success -> { checkUser() }
                                is NetworkResult.Error -> { Timber.e("New Api user was not created %s", newApiUser.message) }
                                else -> { Timber.e("An error occurred") }
                            }
                        }
                    }
                    else -> { Timber.e("An error occurred") }
                }
            }
        }
    }

    /**
     * Creates an API key for the user.
     *
     * This method retrieves the API key for the user and sets up basic authentication.
     */
    private fun createApiKey() {
        val productType = settings.getProductSubscriptionKeys(ProductType.REMITTANCE)
        viewModelScope.launch(Dispatchers.IO) {
            val apiUserKey = this@AppMainViewModel.context.let { Utils.getApiKey(it) }
            if (StringUtils.isNotBlank(apiUserKey)) {
                this@AppMainViewModel.setBasicAuth(BuildConfig.MOMO_API_USER_ID, apiUserKey.toString())
                getAccessToken()
            } else {
                defaultRepository.createApiKey(BuildConfig.MOMO_API_VERSION_V1, productType).collect { apiKey ->
                    when (apiKey) {
                        is NetworkResult.Success -> {
                            try {
                                context.let { Utils.saveApiKey(it, apiKey.response?.apiKey.toString()) }
                                this@AppMainViewModel.setBasicAuth(BuildConfig.MOMO_API_USER_ID, apiUserKey.toString())
                                Timber.d("Api Key fetched and saved successfully")
                                getAccessToken()
                            } catch (exception: Exception) {
                                Timber.d("An Error occurred %s", exception.message)
                            }
                        }
                        is NetworkResult.Error -> {
                            Timber.e("Api Key creation failed %s", apiKey.message)
                        }
                        else -> { Timber.e("Api Key creation failed") }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the access token for the user.
     *
     * This method checks if the access token is available and retrieves it if not.
     */
    private fun getAccessToken() {
        val productType = settings.getProductSubscriptionKeys(ProductType.REMITTANCE)
        viewModelScope.launch(Dispatchers.IO) {
            val apiUserKey = context.let { Utils.getApiKey(it) }
            val accessToken = context.let { Utils.getAccessToken(it) }

            if (StringUtils.isNotBlank(apiUserKey) && StringUtils.isBlank(accessToken)) {
                apiUserKey.let { apiKey ->
                    defaultRepository.getAccessToken(productType, ProductType.REMITTANCE.productType).collect { accessToken ->
                        when (accessToken) {
                            is NetworkResult.Success -> {
                                try {
                                    context.let { activityContext ->
                                        Utils.saveAccessToken(activityContext, accessToken.response)
                                    }
                                    this@AppMainViewModel.setAccessToken(Utils.getAccessToken(context))
                                    Timber.d("Access token created and saved successfully")
                                } catch (exception: Exception) {
                                    Timber.d("An Error occurred %s", exception.message)
                                }
                            }
                            is NetworkResult.Error -> {
                                Timber.e("Access token creation failed %s", accessToken.message)
                            }
                            else -> {
                                Timber.e("Access token creation failed")
                            }
                        }
                    }
                }
            } else {
                this@AppMainViewModel.setAccessToken(Utils.getAccessToken(context))
                Timber.d("A valid access token exists")
            }
        }
    }

    /*    private fun getBasicUserInfo() {
        val accessToken = Utils.getAccessToken(this)
        if (StringUtils.isNotBlank(accessToken)) {
            momoAPI.getBasicUserInfo(
                "46733123459",
                Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                accessToken,
                BuildConfig.MOMO_API_VERSION_V1,
                Constants.ProductTypes.REMITTANCE,
            ) { momoAPIResult ->
                when (momoAPIResult) {
                    is MomoResponse.Success -> {
                        val basicInfo = momoAPIResult.value
                        Timber.d(basicInfo.toString())
                    }
                    is MomoResponse.Failure -> {
                        val momoAPIException = momoAPIResult.APIException
                        toast(momoAPIException?.message ?: "An error occurred!")
                    }
                }
            }
        }
    }

    private fun getUserInfoWithConsent() {
        val accessToken = Utils.getAccessToken(this)
        if (StringUtils.isNotBlank(accessToken)) {
            momoAPI.getUserInfoWithConsent(
                Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                accessToken,
                BuildConfig.MOMO_API_VERSION_V1,
                Constants.ProductTypes.REMITTANCE,
            ) { momoAPIResult ->
                when (momoAPIResult) {
                    is MomoResponse.Success -> {
                        val getUserInfoWithoutConsent = momoAPIResult.value
                        Timber.d(getUserInfoWithoutConsent.toString())
                    }
                    is MomoResponse.Failure -> {
                        val momoAPIException = momoAPIResult.APIException
                        toast(momoAPIException?.message ?: "An error occurred!")
                    }
                }
            }
        }
    }


        private fun getAccessToken() {
            viewModelScope.launch {
                val apiUserKey = context?.let { Utils.getApiKey(it) }
                val accessToken = context?.let { Utils.getAccessToken(it) }
                if (StringUtils.isNotBlank(apiUserKey) && StringUtils.isBlank(accessToken)) {
                    apiUserKey?.let { apiKey ->
                        defaultRepository.getAccessToken(
                            Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                            apiKey,
                            ProductType.REMITTANCE.productType
                        ) { momoAPIResult ->
                            when (momoAPIResult) {
                                is MomoResponse.Success -> {
                                    val generatedAccessToken = momoAPIResult.value
                                    context?.let { activityContext ->
                                        Utils.saveAccessToken(
                                            activityContext,
                                            generatedAccessToken
                                        )
                                    }
                                }
                                is MomoResponse.Failure -> {
                                    val momoAPIException = momoAPIResult.momoException!!
                                }
                            }
                        }
                    }
                } else {
                }
            }
        }

    /**
     * Requests a refund for a transaction.
     *
     * @param requestToPayUuid The UUID of the request to pay.
     */
    fun refund(requestToPayUuid: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        val transactionUuid = Settings().generateUUID()
        if (StringUtils.isNotBlank(accessToken) && StringUtils.isNotBlank(requestToPayUuid)) {
            val creditTransaction = createRefundTransaction(requestToPayUuid)
            accessToken?.let {
                defaultRepository.refund(
                    it,
                    creditTransaction,
                    BuildConfig.MOMO_API_VERSION_V2,
                    Settings().getProductSubscriptionKeys(ProductType.DISBURSEMENTS),
                    transactionUuid
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            getRefundStatus(transactionUuid)
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                        }
                    }
                }
            }
        }
    }

    /**
     * Retrieves the status of a refund transaction.
     *
     * @param referenceId The reference ID of the transaction.
     */
    private fun getRefundStatus(referenceId: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                defaultRepository.getRefundStatus(
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    Settings().getProductSubscriptionKeys(ProductType.DISBURSEMENTS),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            val completeTransfer =
                                Gson().fromJson(momoAPIResult.value!!.source().readUtf8(), MomoTransaction::class.java)
                            Timber.d(completeTransfer.toString())
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates a refund transaction.
     *
     * @param requestToPayUuid The UUID of the request to pay.
     * @return A MomoTransaction object representing the refund transaction.
     */
    private fun createRefundTransaction(requestToPayUuid: String): MomoTransaction {
        return MomoTransaction(
            amount = "30",
            currency = "EUR",
            financialTransactionId = null,
            externalId = Settings().generateUUID(),
            payee = null,
            payer = null,
            payerMessage = "Testing",
            payeeNote = "The Good Company",
            status = null,
            reason = null,
            referenceIdToRefund = requestToPayUuid
        )
    }*/

    /**
     * Emits the state of the SnackBar component.
     *
     * @param snackBarComponentConfiguration The configuration for the SnackBar component.
     */
    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
