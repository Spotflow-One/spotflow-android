package com.spotflow.ui.views
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import com.spotflow.ui.utils.RippleAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SuccessPage(
    paymentOptionsEnum: com.spotflow.models.PaymentOptionsEnum,
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    successMessage: String,
    amount: Number,
    rate: com.spotflow.models.Rate,
    closeSuccessPage: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            delay(2000)
            closeSuccessPage.invoke()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PaymentOptionTile(
            imageRes = paymentOptionsEnum.icon,
            title = paymentOptionsEnum.title
        )
        Spacer(modifier = Modifier.height(8.dp))
        PaymentCard(
            paymentManager = paymentManager,
            rate = rate,
            amount = amount
        )
        Spacer(modifier = Modifier.height(70.dp))
        RippleAnimation()
        Spacer(modifier = Modifier.height(34.dp))
        Text(
            text = successMessage,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            color = Color(0xFF3D3844)
        )
        Spacer(modifier = Modifier.weight(1f))
        PciDssIcon()
        Spacer(modifier = Modifier.height(80.dp))



    }
}




