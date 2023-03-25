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
package com.rekast.momoapi.sample.ui.disbursement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.rekast.momoapi.sample.R
import com.rekast.momoapi.sample.ui.components.general.CircularProgressBarComponent
import com.rekast.momoapi.sample.ui.components.general.PaymentDataScreenComponent
import com.rekast.momoapi.sample.ui.components.general.SnackBarComponent
import com.rekast.momoapi.sample.ui.navigation.drawer.Drawer
import com.rekast.momoapi.sample.ui.navigation.topbar.TopBar
import com.rekast.momoapi.sample.utils.SnackBarComponentConfiguration
import com.rekast.momoapi.sample.utils.SnackBarThemeOptions
import com.rekast.momoapi.sample.utils.hookSnackBar
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Composable
fun DisbursementScreen(
    navController: NavController?,
    snackStateFlow: SharedFlow<SnackBarComponentConfiguration>,
    showProgressBar: Boolean = false,
    disbursementScreenViewModel: DisbursementScreenViewModel?,
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val snackBarTheme = SnackBarThemeOptions()

    LaunchedEffect(Unit) {
        snackStateFlow.hookSnackBar(scaffoldState)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState, title = R.string.disbursement_screen) },
        drawerBackgroundColor = colorResource(id = R.color.accent_secondary),
        drawerContent = {
            navController?.let { Drawer(scope = scope, scaffoldState = scaffoldState, navController = it) }
        },
        drawerGesturesEnabled = true,
        backgroundColor = colorResource(id = R.color.white),
        snackbarHost = { snackBarHostState ->
            SnackBarComponent(
                snackBarHostState = snackBarHostState,
                backgroundColorHex = snackBarTheme.backgroundColor,
                actionColorHex = snackBarTheme.actionTextColor,
                contentColorHex = snackBarTheme.messageTextColor,
            )
        },
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (!showProgressBar) {
                disbursementScreenViewModel?.let {
                    val phoneNumber by disbursementScreenViewModel.phoneNumber.observeAsState("")
                    val financialId by disbursementScreenViewModel.financialId.observeAsState("")
                    val amount by disbursementScreenViewModel.amount.observeAsState("")
                    val paymentMessage by disbursementScreenViewModel.paymentMessage.observeAsState("")
                    val paymentNote by disbursementScreenViewModel.paymentNote.observeAsState("")

                    PaymentDataScreenComponent(
                        title = stringResource(id = R.string.request_to_deposit_title),
                        submitButtonText = stringResource(id = R.string.send_deposit_submit_button),
                        phoneNumber = phoneNumber,
                        financialId = financialId,
                        amount = amount,
                        paymentMessage = paymentMessage,
                        paymentNote = paymentNote,
                        onRequestPayButtonClicked = { disbursementScreenViewModel.deposit() },
                        onPhoneNumberUpdated = { disbursementScreenViewModel.onPhoneNumberUpdated(it) },
                        onFinancialIdUpdated = { disbursementScreenViewModel.onFinancialIdUpdated(it) },
                        onAmountUpdated = { disbursementScreenViewModel.onAmountUpdated(it) },
                        onPayerMessageUpdated = { disbursementScreenViewModel.onPayerMessageUpdated(it) },
                        onPayerNoteUpdated = { disbursementScreenViewModel.onPayerNoteUpdated(it) },
                    )
                }
            } else {
                CircularProgressBarComponent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DisbursementScreenPreview() {
    DisbursementScreen(
        navController = null,
        snackStateFlow = MutableSharedFlow<SnackBarComponentConfiguration>().asSharedFlow(),
        showProgressBar = false,
        disbursementScreenViewModel = null,
    )
}
