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
package io.rekast.sdk.sample.views

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rekast.sdk.BuildConfig
import io.rekast.sdk.model.MomoTransaction
import io.rekast.sdk.model.authentication.credentials.AccessTokenCredentials
import io.rekast.sdk.model.authentication.credentials.BasicAuthCredentials
import io.rekast.sdk.repository.DefaultRepository
import io.rekast.sdk.sample.utils.SnackBarComponentConfiguration
import io.rekast.sdk.sample.utils.Utils
import io.rekast.sdk.utils.ProductType
import io.rekast.sdk.utils.Settings
import javax.inject.Inject
import kotlin.toString
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
 */
@HiltViewModel
open class AppMainViewModel @Inject constructor(
    private val defaultRepository: DefaultRepository,
    @ApplicationContext private val context: Context
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
     */
    fun checkUser() {
        viewModelScope.launch {
            val validUser = defaultRepository.checkApiUser(
                BuildConfig.MOMO_API_VERSION_V1,
                Settings().getProductSubscriptionKeys(ProductType.COLLECTION)
            )

            if (validUser.isSuccessful == true) {
                createApiKey()
            } else {
                Timber.e(validUser.errorBody()?.string())
                val createdUser = defaultRepository.createApiUser(
                    BuildConfig.MOMO_API_VERSION_V1,
                    BuildConfig.MOMO_API_USER_ID,
                    Settings().getProductSubscriptionKeys(ProductType.COLLECTION)
                )
                if (createdUser.isSuccessful == true) {
                    checkUser()
                } else {
                    Timber.e("ApiUser was not created")
                }
            }
        }
    }

    /**
     * Creates an API key for the user.
     */
    private fun createApiKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiUserKey = this@AppMainViewModel.context.let { Utils.getApiKey(it) }
            if (StringUtils.isNotBlank(apiUserKey)) {
                this@AppMainViewModel.setBasicAuth(BuildConfig.MOMO_API_USER_ID, apiUserKey.toString())
                getAccessToken()
            } else {
                val userApiKey = defaultRepository.createApiKey(
                    BuildConfig.MOMO_API_VERSION_V1,
                    Settings().getProductSubscriptionKeys(ProductType.REMITTANCE)
                )
                try {
                    if (userApiKey.isSuccessful == true) {
                        context?.let { Utils.saveApiKey(it, userApiKey.body()?.apiKey.toString()) }
                        this@AppMainViewModel.setBasicAuth(BuildConfig.MOMO_API_USER_ID, apiUserKey.toString())
                        Timber.d("Api Key fetched and saved successfully")
                        getAccessToken()
                    } else {
                        Timber.e("Api Key creation failed %s", userApiKey?.errorBody()?.string())
                    }
                } catch (exception: Exception) {
                }
            }
        }
    }

    /**
     * Retrieves the access token for the user.
     */
    private fun getAccessToken() {
        viewModelScope.launch(Dispatchers.IO) {
            val apiUserKey = context?.let { Utils.getApiKey(it) }
            val accessToken = context?.let { Utils.getAccessToken(it) }

            if (StringUtils.isNotBlank(apiUserKey) && StringUtils.isBlank(accessToken)) {
                apiUserKey?.let { apiKey ->
                    val accessToken = defaultRepository.getAccessToken(
                        Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                        ProductType.REMITTANCE.productType
                    )
                    try {
                        if (accessToken?.isSuccessful == true) {
                            context?.let { activityContext ->
                                Utils.saveAccessToken(
                                    activityContext,
                                    accessToken.body()
                                )
                            }
                            Timber.d("Access token created and saved successfully")
                        } else {
                            Timber.e("Access token creation failed %s", accessToken?.errorBody()?.string())
                        }
                    } catch (exception: Exception) {
                    }
                }
            } else {
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
    }*/

    /*    private fun getApiKey() {
            viewModelScope.launch {
                val apiUserKey = context?.let { Utils.getApiKey(it) }
                if (StringUtils.isNotBlank(apiUserKey)) {
                    getAccessToken()
                } else {
                    defaultRepository.getUserApiKey(
                        Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                        BuildConfig.MOMO_API_VERSION_V1
                    ) { momoAPIResult ->
                        when (momoAPIResult) {
                            is MomoResponse.Success -> {
                                val generatedApiUserKey = momoAPIResult.value
                                context?.let { Utils.saveApiKey(it, generatedApiUserKey.apiKey) }
                                getAccessToken()
                            }
                            is MomoResponse.Failure -> {
                                val momoAPIException = momoAPIResult.momoException!!
                            }
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
        }*/

    /**
     * Creates a refund transaction.
     *
     * @param requestToPayUuid The UUID of the request to pay.
     * @return A MomoTransaction object representing the refund transaction.
     */
    private fun createRefundTransaction(requestToPayUuid: String): MomoTransaction {
        return MomoTransaction(
            "30",
            "EUR",
            null,
            Settings().generateUUID(),
            null,
            null,
            "Testing",
            "The Good Company",
            null,
            null,
            requestToPayUuid
        )
    }

    /**
     * Emits the state of the SnackBar component.
     *
     * @param snackBarComponentConfiguration The configuration for the SnackBar component.
     */
    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
