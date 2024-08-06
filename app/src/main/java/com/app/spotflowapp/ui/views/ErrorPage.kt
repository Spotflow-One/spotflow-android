package com.app.spotflowapp.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.spotflowapp.models.PaymentOptionsEnum
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PaymentOptionTile
import com.app.spotflowapp.R
import com.app.spotflowapp.models.examplePaymentManager
import com.app.spotflowapp.ui.theme.SpotFlowAppTheme
import com.app.spotflowapp.ui.utils.PciDssIcon

@Composable
fun ErrorPage(
    paymentManager: SpotFlowPaymentManager,
    message: String,
    paymentOptionsEnum: PaymentOptionsEnum,
    rate: Rate?,
    onTryAgainWithCardClick: () -> Unit,
    onTryAgainWithTransferClick: () -> Unit,
    onTryAgainWithUssdClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentOptionTile(
            title = paymentOptionsEnum.title,
            imageRes = paymentOptionsEnum.icon
        )
        PaymentCard(
            paymentManager = paymentManager,
            rate = rate
        )
        Spacer(modifier = Modifier.height(49.dp))
        Image(painter = painterResource(R.drawable.warning_icon), contentDescription = null, modifier = Modifier.height(70.dp).width(70.dp))
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = message,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF55515B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 30.dp)
        )
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            onClick = onTryAgainWithCardClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Try again with Card",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B),
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onTryAgainWithTransferClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Try again with transfer",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B),
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onTryAgainWithUssdClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Try again with USSD",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B),
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PciDssIcon() // Replace with actual composable
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotFlowAppTheme {
        ErrorPage(
            paymentManager = examplePaymentManager,
            paymentOptionsEnum = PaymentOptionsEnum.CARD,
            message = "Failure",
            rate = null,
            onTryAgainWithCardClick = {},
            onTryAgainWithTransferClick = {},
            onTryAgainWithUssdClick = {}
        )
    }
}

