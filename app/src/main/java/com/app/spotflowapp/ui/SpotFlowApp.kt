package com.app.spotflowapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.spotflowapp.R
import com.app.spotflowapp.models.PaymentOptionsEnum
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.SpotFlowScreen
import com.app.spotflowapp.ui.views.EnterCardDetail
import com.app.spotflowapp.ui.views.EnterCardOtp
import com.app.spotflowapp.ui.views.EnterCardPin
import com.app.spotflowapp.ui.views.ErrorPage
import com.app.spotflowapp.ui.views.HomeView
import com.app.spotflowapp.ui.views.SuccessPage
import com.app.spotflowapp.ui.views.TransferHomeView
import com.app.spotflowapp.ui.views.TransferInfoView
import com.app.spotflowapp.ui.views.TransferStatusCheckPage

class SpotFlowApp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotFlowApp(
    viewModel: SpotFlowViewModel,
    navController: NavHostController = rememberNavController(),

    ) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = SpotFlowScreen.valueOf(
        backStackEntry?.destination?.route ?: SpotFlowScreen.HomeView.name
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                modifier = Modifier.height(80.dp),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFF4F4FF)
                ),
                navigationIcon = {
                    Image(
                        painter = painterResource(R.drawable.nba_logo),
                        contentDescription = null,
                        modifier = Modifier.width(51.dp).height(51.dp)
                            .padding(top = 17.dp, start = 14.dp)
                    )
                }
            )
        }
    ) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = SpotFlowScreen.HomeView.name,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = SpotFlowScreen.HomeView.name) {
                HomeView(
                    paymentManager = uiState.paymentManager,
                    onPayWithTransferClicked = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.TRANSFER)
                        navController.navigate(SpotFlowScreen.TransferHomeView.name)
                    },
                    onPayWithCardClicked = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.CARD)

                        navController.navigate(SpotFlowScreen.EnterCardDetailsView.name)
                    },
                    onPayWithUSSDClicked = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.USSD)
//                    navController.navigate(SpotFlowScreen.UssdView.name)
                    },
                    onCancelButtonClicked = {

                    },
                    onRateFetched = {
                        viewModel.setRate(it)
                    }
                )
            }

            composable(route = SpotFlowScreen.TransferHomeView.name) {
                TransferHomeView(
                    paymentManager = uiState.paymentManager,
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCreatedPayment = {
                        viewModel.setPaymentResponseBody(it)
                    },
                    onIveSentTheMoneyClicked = {
                        navController.navigate(SpotFlowScreen.TransferInfoView.name)
                    }
                )
            }

            composable(route = SpotFlowScreen.TransferInfoView.name) {
                TransferInfoView(
                    paymentManager = uiState.paymentManager,
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    rate = uiState.paymentResponseBody?.rate,
                    onKeepWaitingClicked = {
                        navController.navigate(SpotFlowScreen.TransferStatusCheckView.name)
                    }
                )
            }

            composable(route = SpotFlowScreen.TransferStatusCheckView.name) {
                TransferStatusCheckPage(
                    paymentManager = uiState.paymentManager,
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    onSuccessfulPayment = {
                        navController.navigate(SpotFlowScreen.SuccessView.name)
                    }
                )
            }

            composable(route = SpotFlowScreen.SuccessView.name) {
                SuccessPage(
                    paymentManager = uiState.paymentManager,
                    paymentOptionsEnum = uiState.paymentOptionsEnum!!,
                    rate = uiState.paymentResponseBody!!.rate!!,
                    successMessage = "Payment successful",
                    closeSuccessPage = {
                        changePayment(navController = navController, viewModel = viewModel)
                    }

                )
            }

            composable(route = SpotFlowScreen.EnterCardDetailsView.name) {
                EnterCardDetail(
                    paymentManager = uiState.paymentManager,
                    rate = uiState.rate,
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    }

                )
            }
            composable(route = SpotFlowScreen.EnterCardOtpView.name) {
                EnterCardOtp(
                    paymentManager = uiState.paymentManager,
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    },
                    paymentResponseBody = uiState.paymentResponseBody!!,
                )
            }


            composable(route = SpotFlowScreen.EnterCardPinView.name) {
                EnterCardPin(
                    paymentManager = uiState.paymentManager,
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    onCancelButtonClicked = {
                        closeSpotFlow()
                    },
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    }

                )
            }

            composable(route = SpotFlowScreen.ErrorView.name) {
                ErrorPage(
                    paymentManager = uiState.paymentManager,
                    rate = uiState.paymentResponseBody?.rate,
                    onTryAgainWithUssdClick = {

                    },
                    onTryAgainWithCardClick = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.CARD)
                        navController.navigate(SpotFlowScreen.EnterCardDetailsView.name)
                    },
                    onTryAgainWithTransferClick = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.TRANSFER)
                        navController.navigate(SpotFlowScreen.TransferHomeView.name)
                    },
                    message = uiState.paymentResponseBody?.providerMessage ?: "Payment failed",
                    paymentOptionsEnum = uiState.paymentOptionsEnum!!,
                )
            }


        }
    }
}

private fun changePayment(
    navController: NavHostController,
    viewModel: SpotFlowViewModel,
) {
    viewModel.resetPayment()
    navController.popBackStack(SpotFlowScreen.HomeView.name, inclusive = false)
}

private fun closeSpotFlow() {

}

private fun handleCardSuccessResponse(
    navController: NavHostController,
    viewModel: SpotFlowViewModel,
) {
    val paymentResponseBody = viewModel.uiState.value.paymentResponseBody!!

    if (paymentResponseBody.status == "successful") {
        navController.navigate(SpotFlowScreen.SuccessView.name)
    } else if (paymentResponseBody.authorization?.mode == "pin") {
        navController.navigate(SpotFlowScreen.EnterCardPinView.name)
    } else if (paymentResponseBody.authorization?.mode == "otp") {
        navController.navigate(SpotFlowScreen.EnterCardOtpView.name)

    } else if (paymentResponseBody.authorization?.mode == "3DS") {
        //todo:open browser.
    } else {
        navController.navigate(SpotFlowScreen.ErrorView.name)
    }
}
