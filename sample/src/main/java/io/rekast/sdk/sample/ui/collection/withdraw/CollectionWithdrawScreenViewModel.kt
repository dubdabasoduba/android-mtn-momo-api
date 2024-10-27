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
package io.rekast.sdk.sample.ui.collection.withdraw

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.rekast.sdk.model.api.MomoTransaction
import io.rekast.sdk.network.api.route.Routes
import io.rekast.sdk.sample.activity.AppMainViewModel
import io.rekast.sdk.sample.utils.Constants
import io.rekast.sdk.sample.utils.SnackBarComponentConfiguration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CollectionWithdrawScreenViewModel : ViewModel() {
    var context: Context? = null
    private var momoAPi: Routes? = null
    val showProgressBar = MutableLiveData(false)
    private lateinit var _appMainViewModel: AppMainViewModel
    var momoTransaction: MutableLiveData<MomoTransaction?> = MutableLiveData(null)
    private val _snackBarStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>()
    val snackBarStateFlow: SharedFlow<SnackBarComponentConfiguration> = _snackBarStateFlow.asSharedFlow()

    private val _phoneNumber = MutableLiveData(Constants.EMPTY_STRING)
    val phoneNumber: LiveData<String>
        get() = _phoneNumber

    private val _financialId = MutableLiveData(Constants.EMPTY_STRING)
    val financialId: LiveData<String>
        get() = _financialId

    private val _referenceIdToRefund = MutableLiveData(Constants.EMPTY_STRING)
    val referenceIdToRefund: LiveData<String>
        get() = _referenceIdToRefund

    private val _amount = MutableLiveData(Constants.EMPTY_STRING)
    val amount: LiveData<String>
        get() = _amount

    private val _payerMessage = MutableLiveData(Constants.EMPTY_STRING)
    val paymentMessage: LiveData<String>
        get() = _payerMessage

    private val _payerNote = MutableLiveData(Constants.EMPTY_STRING)
    val paymentNote: LiveData<String>
        get() = _payerNote

    private val _deliveryNote = MutableLiveData(Constants.EMPTY_STRING)
    val deliveryNote: LiveData<String>
        get() = _deliveryNote

    fun onPhoneNumberUpdated(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun onFinancialIdUpdated(financialId: String) {
        _financialId.value = financialId
    }

    fun onAmountUpdated(amount: String) {
        _amount.value = amount
    }

    fun onPayerMessageUpdated(payerMessage: String) {
        _payerMessage.value = payerMessage
    }

    fun onPayerNoteUpdated(payerNote: String) {
        _payerNote.value = payerNote
    }

    fun onDeliveryNoteUpdated(deliveryNote: String) {
        _deliveryNote.value = deliveryNote
    }

    fun onReferenceIdToRefundUpdated(deliveryNote: String) {
        _referenceIdToRefund.value = deliveryNote
    }

/*    fun requestToWithdraw() {
        showProgressBar.postValue(true)
        if (phoneNumber.value!!.isNotEmpty() && financialId.value!!.isNotEmpty() &&
            amount.value!!.isNotEmpty() && paymentMessage.value!!.isNotEmpty() &&
            paymentNote.value!!.isNotEmpty()
        ) {
            val accessToken = context?.let { Utils.getAccessToken(it) }
            val creditTransaction = createRequestToWithdrawTransaction()
            val transactionUuid = Settings().generateUUID()
            if (StringUtils.isNotBlank(accessToken)) {
                accessToken?.let {
                    momoAPi?.requestToWithdraw(
                        it,
                        creditTransaction,
                        BuildConfig.MOMO_API_VERSION_V2,
                        Settings().getProductSubscriptionKeys(ProductType.COLLECTION),
                        transactionUuid
                    ) { momoAPIResult ->
                        when (momoAPIResult) {
                            is MomoResponse.Success -> {
                                requestToPayDeliveryNotification(
                                    transactionUuid
                                )
                                requestToWithdrawTransactionStatus(transactionUuid)
                                showProgressBar.postValue(false)
                                emitSnackBarState(
                                    SnackBarComponentConfiguration(
                                        message = "Request to withdraw sent successfully"
                                    )
                                )
                            }
                            is MomoResponse.Failure -> {
                                val momoAPIException = momoAPIResult.momoException
                                showProgressBar.postValue(false)
                                emitSnackBarState(
                                    SnackBarComponentConfiguration(
                                        message = "${momoAPIException!!.message} Request to withdraw wasn't sent!"
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
    }

    private fun requestToWithdrawTransactionStatus(referenceId: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        if (StringUtils.isNotBlank(accessToken)) {
            accessToken?.let {
                momoAPi?.requestToWithdrawTransactionStatus(
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    Settings().getProductSubscriptionKeys(ProductType.COLLECTION),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            val requestToWithdrawStatusFetch =
                                Gson().fromJson(momoAPIResult.value!!.source().readUtf8(), MomoTransaction::class.java)
                            momoTransaction = MutableLiveData(requestToWithdrawStatusFetch)
                            showProgressBar.postValue(false)
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Request to withdraw status fetched successfully"
                                )
                            )
                        }
                        is MomoResponse.Failure -> {
                            showProgressBar.postValue(false)
                            val momoAPIException = momoAPIResult.momoException
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "${momoAPIException!!.message} Request to withdraw status not fetch!"
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

    private fun createRequestToWithdrawTransaction(): MomoTransaction {
        return MomoTransaction(
            amount.value!!.toString(),
            Constants.SANDBOX_CURRENCY,
            financialId.value!!.toString(),
            RandomStringUtils.randomAlphanumeric(Constants.STRING_LENGTH),
            null,
            AccountHolder(AccountHolderType.MSISDN.name, phoneNumber.value!!.toString()),
            paymentMessage.value!!.toString(),
            paymentNote.value!!.toString(),
            null,
            null
        )
    }

    private fun requestToPayDeliveryNotification(referenceId: String) {
        val accessToken = context?.let { Utils.getAccessToken(it) }
        val momoNotification = MomoNotification(
            notificationMessage = deliveryNote.value!!.toString()
        )
        if (StringUtils.isNotBlank(accessToken) &&
            Settings().checkNotificationMessageLength(momoNotification.notificationMessage)
        ) {
            accessToken?.let {
                momoAPi?.requestToPayDeliveryNotification(
                    momoNotification,
                    referenceId,
                    BuildConfig.MOMO_API_VERSION_V1,
                    ProductType.COLLECTION.productType,
                    Settings().getProductSubscriptionKeys(ProductType.COLLECTION),
                    it
                ) { momoAPIResult ->
                    when (momoAPIResult) {
                        is MomoResponse.Success -> {
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "Request to pay delivery note sent successfully"
                                )
                            )
                        }
                        is MomoResponse.Failure -> {
                            val momoAPIException = momoAPIResult.momoException
                            emitSnackBarState(
                                SnackBarComponentConfiguration(
                                    message = "${momoAPIException!!.message} Delivery note was not sent!"
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
    }*/

    fun provideContext(fragmentContext: Context) {
        context = fragmentContext
    }

    fun provideMomoAPI(fragmentMomoAPI: Routes) {
        momoAPi = fragmentMomoAPI
    }

    fun provideAppMainViewModel(appMainViewModel: AppMainViewModel) {
        _appMainViewModel = appMainViewModel
    }

    private fun emitSnackBarState(snackBarComponentConfiguration: SnackBarComponentConfiguration) {
        viewModelScope.launch { _snackBarStateFlow.emit(snackBarComponentConfiguration) }
    }
}
