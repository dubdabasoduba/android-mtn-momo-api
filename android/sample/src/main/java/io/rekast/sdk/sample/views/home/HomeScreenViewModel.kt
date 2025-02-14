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
package io.rekast.sdk.sample.views.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rekast.sdk.BuildConfig
import io.rekast.sdk.model.AccountBalance
import io.rekast.sdk.model.AccountHolder
import io.rekast.sdk.model.AccountHolderStatus
import io.rekast.sdk.model.BasicUserInfo
import io.rekast.sdk.repository.DefaultRepository
import io.rekast.sdk.repository.data.NetworkResult
import io.rekast.sdk.sample.utils.SnackBarComponentConfiguration
import io.rekast.sdk.sample.utils.Utils
import io.rekast.sdk.utils.AccountHolderType
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

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val defaultRepository: DefaultRepository,
    @ApplicationContext private val context: Context,
    private val settings: Settings
) : ViewModel() {
    val showProgressBar = MutableLiveData(false)
    private val _snackBarStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>()
    val snackBarStateFlow: SharedFlow<SnackBarComponentConfiguration> = _snackBarStateFlow.asSharedFlow()
    var basicUserInfo: MutableLiveData<BasicUserInfo?> = MutableLiveData(null)
    var accountHolderStatus: MutableLiveData<AccountHolderStatus?> = MutableLiveData(null)
    var accountBalance: MutableLiveData<AccountBalance?> = MutableLiveData(null)
    val accessToken = context.let { Utils.getAccessToken(it) }

    fun getBasicUserInfo() {
        showProgressBar.postValue(true)
        val productType = Utils.getProductSubscriptionKeys(ProductType.REMITTANCE)

        viewModelScope.launch(Dispatchers.IO) {
            if (StringUtils.isNotBlank(accessToken)) {
                defaultRepository.getBasicUserInfo(
                    productType = ProductType.REMITTANCE.productType,
                    apiVersion = BuildConfig.MOMO_API_VERSION_V1,
                    accountHolder = "99733123459",
                    productSubscriptionKey = productType,
                    environment = BuildConfig.MOMO_ENVIRONMENT
                ).collect { foundBasicUserInfo ->
                    when (foundBasicUserInfo) {
                        is NetworkResult.Success -> {
                            val userInfo = foundBasicUserInfo.response
                            val date = Utils.convertToDate(userInfo?.updatedAt!!.toLong())
                            userInfo.updatedAt = date
                            basicUserInfo.postValue(userInfo)

                            Timber.d("Basic user info was fetched successfully")
                            showProgressBar.postValue(false)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Basic user info was fetched successfully")
                            )
                        }

                        is NetworkResult.Error -> {
                            Timber.e("Basic user info was not fetched %s", foundBasicUserInfo.message)
                            showProgressBar.postValue(false)

                            val message = foundBasicUserInfo.message
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Basic user info was not fetched $message")
                            )
                        }

                        else -> {
                            Timber.e("An error occurred!!")
                        }
                    }
                }
            }
        }
    }

    fun getUserInfoWithConsent() {
        showProgressBar.postValue(true)
        val productType = Utils.getProductSubscriptionKeys(productType = ProductType.REMITTANCE)

        viewModelScope.launch(Dispatchers.IO) {
            if (StringUtils.isNotBlank(accessToken)) {
                defaultRepository.getUserInfoWithConsent(
                    productType = ProductType.REMITTANCE.productType,
                    apiVersion = BuildConfig.MOMO_API_VERSION_V1,
                    productSubscriptionKey = productType,
                    environment = BuildConfig.MOMO_ENVIRONMENT
                ).collect { userInfoWithConsent ->
                    when (userInfoWithConsent) {
                        is NetworkResult.Success -> {
                            Timber.d(userInfoWithConsent.response.toString())

                            Timber.d("Basic user info with consent was fetched successfully")
                            showProgressBar.postValue(false)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Basic user info with consent was fetched successfully")
                            )
                        }

                        is NetworkResult.Error -> {
                            Timber.e("Basic user info with consent was not fetched %s", userInfoWithConsent.message)
                            showProgressBar.postValue(false)

                            val message = userInfoWithConsent.message
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Basic user info with consent was not fetched $message")
                            )
                        }

                        is NetworkResult.Loading -> TODO()
                    }
                }
            }
        }
    }

    fun validateAccountHolderStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar.postValue(true)
            val accountHolder = AccountHolder(
                partyId = "99733123459",
                partyIdType = AccountHolderType.MSISDN.accountHolderType
            )
            if (StringUtils.isNotBlank(accessToken)) {
                defaultRepository.validateAccountHolderStatus(
                    productType = ProductType.REMITTANCE.productType,
                    apiVersion = BuildConfig.MOMO_API_VERSION_V1,
                    accountHolder = accountHolder,
                    productSubscriptionKey = Utils.getProductSubscriptionKeys(ProductType.REMITTANCE),
                    environment = BuildConfig.MOMO_ENVIRONMENT
                ).collect { foundStatus ->
                    when (foundStatus) {
                        is NetworkResult.Success -> {
                            val status = Gson().fromJson(foundStatus.response!!.source().readUtf8(), AccountHolderStatus::class.java)
                            accountHolderStatus.postValue(status)

                            Timber.d("Account Holder status was fetched successfully")
                            showProgressBar.postValue(false)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Account Holder status was fetched successfully")
                            )
                        }
                        is NetworkResult.Error -> {
                            Timber.e("Account Holder status was not fetched %s", foundStatus.message)
                            showProgressBar.postValue(false)

                            val message = foundStatus.message
                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Account Holder status was not fetched $message")
                            )
                        }

                        else -> { Timber.e("An error occurred!!") }
                    }
                }
            } else {
                showProgressBar.postValue(false)
                emitSnackBarState(
                    SnackBarComponentConfiguration(
                        message = "Expired access token! Please refresh the token"
                    )
                )
            }
        }
    }

    /**
     * Only works with the Collection API
     * */
    fun getAccountBalance() {
        viewModelScope.launch(Dispatchers.IO) {
            showProgressBar.postValue(true)
            if (StringUtils.isNotBlank(accessToken)) {
                defaultRepository.getAccountBalance(
                    productType = ProductType.COLLECTION.productType,
                    apiVersion = BuildConfig.MOMO_API_VERSION_V1,
                    currency = "",
                    productSubscriptionKey = Utils.getProductSubscriptionKeys(ProductType.COLLECTION),
                    environment = BuildConfig.MOMO_ENVIRONMENT
                ).collect { balance ->
                    when (balance) {
                        is NetworkResult.Success<*> -> {
                            val foundBalance = balance.response
                            accountBalance.postValue(foundBalance)

                            Timber.d("Account balance fetched successfully")
                            showProgressBar.postValue(false)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Account balance fetched successfully"
                                )
                            )
                        }
                        is NetworkResult.Error<*> -> {
                            showProgressBar.postValue(false)

                            val message = balance.message
                            Timber.e("Account balance not fetched! %s", message)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Account balance not fetched! $message"
                                )
                            )
                        }

                        else -> { Timber.e("An error occurred!!") }
                    }
                }
            } else {
                showProgressBar.postValue(false)
                emitSnackBarState(
                    SnackBarComponentConfiguration(
                        message = "Expired access token! Please refresh the token"
                    )
                )
            }
        }
    }
    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
