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
package io.rekast.sdk.sample.views.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rekast.sdk.model.AccountBalance
import io.rekast.sdk.model.AccountHolderStatus
import io.rekast.sdk.model.BasicUserInfo
import io.rekast.sdk.repository.DefaultRepository
import io.rekast.sdk.sample.utils.SnackBarComponentConfiguration
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeScreenViewModel @Inject constructor(private val defaultRepository: DefaultRepository, @ApplicationContext private val context: Context) : ViewModel() {
    val showProgressBar = MutableLiveData(false)
    private val _snackBarStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>()
    val snackBarStateFlow: SharedFlow<SnackBarComponentConfiguration> = _snackBarStateFlow.asSharedFlow()
    var basicUserInfo: MutableLiveData<BasicUserInfo?> = MutableLiveData(null)
    var accountHolderStatus: MutableLiveData<AccountHolderStatus?> = MutableLiveData(null)
    var accountBalance: MutableLiveData<AccountBalance?> = MutableLiveData(null)

/*    fun getBasicUserInfo() {
        showProgressBar.postValue(true)
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi!!.getBasicUserInfo(
                    "99733123459",
                    Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                    it,
                    BuildConfig.MOMO_API_VERSION_V1,
                    ProductType.REMITTANCE.productType
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            val date = Utils.convertToDate(momoAPIResult.value.updatedAt.toLong())
                            momoAPIResult.value.updatedAt = date
                            basicUserInfo.postValue(momoAPIResult.value)
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(message = "Basic ApiUser info fetched successfully")
                            )
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "${momoAPIException!!.message} " + "Basic ApiUser" +
                                        " info not fetched! Please try again"
                                )
                            )
                        }
                    }
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

    fun validateAccountHolderStatus() {
        showProgressBar.postValue(true)
        val accessToken = context?.let { Utils.getAccessToken(it) }
        val accountHolder = AccountHolder(
            partyId = "346736732646",
            partyIdType = AccountHolderType.MSISDN.accountHolderType
        )
        if (StringUtils.isNotBlank(accessToken)) {
            if (accessToken != null) {
                momoAPi?.validateAccountHolderStatus(
                    accountHolder,
                    BuildConfig.MOMO_API_VERSION_V1,
                    ProductType.REMITTANCE.productType,
                    Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                    accessToken
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            val accountHolderStatus = Gson().fromJson(
                                momoAPIResult.value!!.source().readUtf8(),
                                AccountHolderStatus::class.java
                            )
                            _accountHolderStatus.postValue(accountHolderStatus)
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Account status fetched successfully"
                                )
                            )
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "${momoAPIException!!.message} Account status not fetched!"
                                )
                            )
                            Timber.d(momoAPIException)
                        }
                    }
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

    fun getAccountBalance() {
        showProgressBar.postValue(true)
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi?.getBalance(
                    Constants.SANDBOX_CURRENCY,
                    Settings().getProductSubscriptionKeys(ProductType.REMITTANCE),
                    it,
                    BuildConfig.MOMO_API_VERSION_V1,
                    ProductType.REMITTANCE.productType
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            val balance = momoAPIResult.value
                            _accountBalance.postValue(balance)
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Account balance fetched successfully"
                                )
                            )
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                            showProgressBar.postValue(false)

                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "${momoAPIException!!.message} Account balance not fetched!"
                                )
                            )

                            Timber.d(momoAPIException)
                        }
                    }
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
    }*/

    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
