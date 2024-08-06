package com.app.spotflowapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.app.spotflowapp.models.PaymentOptionsEnum
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.SpotFlowScreen
import com.app.spotflowapp.models.examplePaymentManager
import com.app.spotflowapp.models.examplePaymentResponseBody
import com.app.spotflowapp.ui.SpotFlowApp
import com.app.spotflowapp.ui.SpotFlowViewModel
import com.app.spotflowapp.ui.theme.SpotFlowAppTheme
import com.app.spotflowapp.ui.utils.BankAccountCard
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PinInputView
import com.app.spotflowapp.ui.utils.RippleAnimation
import com.app.spotflowapp.ui.views.EnterCardDetail
import com.app.spotflowapp.ui.views.EnterCardOtp
import com.app.spotflowapp.ui.views.EnterCardPin
import com.app.spotflowapp.ui.views.HomeView
import com.app.spotflowapp.ui.views.SuccessPage
import com.app.spotflowapp.ui.views.TransferHomeView
import com.app.spotflowapp.ui.views.TransferInfoView
import com.app.spotflowapp.ui.views.TransferStatusCheckPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotFlowAppTheme {
                SpotFlowApp(
                    viewModel = SpotFlowViewModel(paymentManager = examplePaymentManager)
                )
//             TransferStatusCheckPage(paymentManager = examplePaymentManager, paymentResponseBody = examplePaymentResponseBody)
            }
        }
    }
}

