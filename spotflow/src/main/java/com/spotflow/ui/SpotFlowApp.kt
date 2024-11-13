package com.spotflow.ui

import ViewBanksUssdPage
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.spotflow.R
import com.spotflow.models.PaymentOptionsEnum
import com.spotflow.models.SpotFlowScreen
import com.spotflow.ui.views.CopyUssdCodePage
import com.spotflow.ui.views.EnterBillingAddressPage
import com.spotflow.ui.views.EnterCardDetail
import com.spotflow.ui.views.EnterCardOtp
import com.spotflow.ui.views.EnterCardPin
import com.spotflow.ui.views.ErrorPage
import com.spotflow.ui.views.HomeView
import com.spotflow.ui.views.SuccessPage
import com.spotflow.ui.views.TransferHomeView
import com.spotflow.ui.views.TransferInfoView
import com.spotflow.ui.views.TransferStatusCheckPage
import com.spotflow.ui.views.WebViewPage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotFlowApp(
    viewModel: SpotFlowViewModel,
    navController: NavHostController = rememberNavController(),
    closeSpotflow: () -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("") },
            modifier = Modifier.height(70.dp),
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = Color(0xFFF4F4FF)
            ),
            navigationIcon = {
                Image(
                    painter = painterResource(R.drawable.nba_logo),
                    contentDescription = null,
                    modifier = Modifier.width(100.dp).height(100.dp).fillMaxHeight()
                        .padding(top = 17.dp, start = 14.dp, bottom = 5.dp)
                )
            })
    }) { innerPadding ->

        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = SpotFlowScreen.HomeView.name,
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(innerPadding)
        ) {
            composable(route = SpotFlowScreen.HomeView.name) {
                HomeView(paymentManager = uiState.paymentManager, onPayWithTransferClicked = {
                    viewModel.setPaymentOptionEnum(PaymentOptionsEnum.TRANSFER)
                    navController.navigate(SpotFlowScreen.TransferHomeView.name)
                }, onPayWithCardClicked = {
                    viewModel.setPaymentOptionEnum(PaymentOptionsEnum.CARD)

                    navController.navigate(SpotFlowScreen.EnterCardDetailsView.name)
                }, onPayWithUSSDClicked = {
                    viewModel.setPaymentOptionEnum(PaymentOptionsEnum.USSD)
                    navController.navigate(SpotFlowScreen.UssdView.name)

                }, onCancelButtonClicked = {
                    closeSpotflow()
                }, onRateFetched = {
                    viewModel.setMerchantConfig(it)
                })
            }

            composable(route = SpotFlowScreen.EnterCardBillingAddress.name) {
                EnterBillingAddressPage(paymentManager = uiState.paymentManager,
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    rate = uiState.rate,
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    })
            }
            composable(route = SpotFlowScreen.TransferHomeView.name) {
                TransferHomeView(
                    paymentManager = uiState.paymentManager,
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCreatedPayment = {
                        viewModel.setPaymentResponseBody(it)
                    },
                    onIveSentTheMoneyClicked = {
                        navController.navigate(SpotFlowScreen.TransferInfoView.name)
                    },
                    merchantConfig = uiState.merchantConfig!!,
                )
            }

            composable(route = SpotFlowScreen.UssdView.name) {
                ViewBanksUssdPage(
                    paymentManager = uiState.paymentManager,
                    rate = uiState.rate,
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    amount = uiState.amount,
                    createPayment = {
                        viewModel.setBank(it)
                        navController.navigate(SpotFlowScreen.CopyUssdView.name)
                    },
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                )
            }


            composable(route = SpotFlowScreen.TransferInfoView.name) {
                TransferInfoView(paymentManager = uiState.paymentManager, onChangePaymentClicked = {
                    changePayment(navController = navController, viewModel = viewModel)
                }, onCancelButtonClicked = {
                    closeSpotflow()
                }, rate = uiState.rate, amount = uiState.amount, onKeepWaitingClicked = {
                    navController.navigate(SpotFlowScreen.TransferStatusCheckView.name)
                })
            }

            composable(route = SpotFlowScreen.TransferStatusCheckView.name) {
                TransferStatusCheckPage(paymentManager = uiState.paymentManager,
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    rate = uiState.rate,
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    paymentOptionsEnum = uiState.paymentOptionsEnum!!,
                    onSuccessfulPayment = {
                        navController.navigate(SpotFlowScreen.SuccessView.name)
                    },
                    onFailedPayment = {
                        navController.navigate(SpotFlowScreen.ErrorView.name)
                    })
            }

            composable(route = SpotFlowScreen.SuccessView.name) {
                SuccessPage(paymentManager = uiState.paymentManager,
                    paymentOptionsEnum = uiState.paymentOptionsEnum!!,
                    rate = uiState.rate,
                    successMessage = "Payment successful",
                    amount = uiState.amount,
                    closeSuccessPage = {
                        changePayment(navController = navController, viewModel = viewModel)
                    }

                )
            }

            composable(route = SpotFlowScreen.EnterCardDetailsView.name) {
                EnterCardDetail(paymentManager = uiState.paymentManager,
                    rate = uiState.merchantConfig!!.rate,
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    amount = uiState.amount,
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
                        closeSpotflow()
                    },
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    },
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    rate = uiState.rate,
                )
            }


            composable(route = SpotFlowScreen.EnterCardPinView.name) {
                EnterCardPin(paymentManager = uiState.paymentManager,
                    paymentResponseBody = uiState.paymentResponseBody!!,
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    rate = uiState.rate,
                    onSuccessfulPayment = {
                        viewModel.setPaymentResponseBody(it)
                        handleCardSuccessResponse(navController, viewModel)
                    }

                )
            }

            composable(route = SpotFlowScreen.ErrorView.name) {
                ErrorPage(
                    paymentManager = uiState.paymentManager,
                    rate = uiState.rate,
                    onTryAgainWithUssdClick = {
                        viewModel.setPaymentOptionEnum(PaymentOptionsEnum.USSD)
                        navController.navigate(SpotFlowScreen.UssdView.name)
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
                    amount = uiState.amount
                )
            }

            composable(
                route = SpotFlowScreen.WebView.name
            ) {
                WebViewPage(url = uiState.paymentResponseBody?.authorization?.redirectUrl ?: "",
                    onComplete = {
                        navController.navigate(SpotFlowScreen.TransferStatusCheckView.name)
                    })
            }
            composable(
                route = SpotFlowScreen.CopyUssdView.name,
            ) {
                CopyUssdCodePage(
                    paymentManager = uiState.paymentManager,
                    rate = uiState.rate,
                    onCancelButtonClicked = {
                        closeSpotflow()
                    },
                    onConfirmPayment = {
                        viewModel.setPaymentResponseBody(it)
                        navController.navigate(SpotFlowScreen.TransferStatusCheckView.name)
                    },
                    onChangePaymentClicked = {
                        changePayment(navController = navController, viewModel = viewModel)
                    },
                    bank = uiState.bank!!,
                    merchantConfig = uiState.merchantConfig!!,
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
        navController.navigate(SpotFlowScreen.WebView.name)
    } else if (paymentResponseBody.authorization?.mode == "avs") {
        navController.navigate(SpotFlowScreen.EnterCardBillingAddress.name)
    } else {
        navController.navigate(SpotFlowScreen.ErrorView.name)
    }
}
