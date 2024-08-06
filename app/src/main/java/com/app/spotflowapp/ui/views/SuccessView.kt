package com.app.spotflowapp.ui.views
import  com.app.spotflowapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.spotflowapp.models.PaymentOptionsEnum
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PaymentOptionTile
import com.app.spotflowapp.ui.utils.PciDssIcon
import com.app.spotflowapp.ui.utils.RippleAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SuccessPage(
    paymentOptionsEnum: PaymentOptionsEnum,
    paymentManager: SpotFlowPaymentManager,
    successMessage: String,
    rate: Rate? = null,
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
            rate = rate
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




