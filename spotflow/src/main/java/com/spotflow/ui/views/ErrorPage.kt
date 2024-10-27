package com.spotflow.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import com.spotflow.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import java.time.temporal.TemporalAmount

@Composable
fun ErrorPage(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    message: String,
    amount: Number,
    paymentOptionsEnum: com.spotflow.models.PaymentOptionsEnum,
    rate: com.spotflow.models.Rate,
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
            rate = rate,
            amount = amount,
        )
        Spacer(modifier = Modifier.height(49.dp))
        Image(
            painter = painterResource(R.drawable.warning_icon),
            contentDescription = null,
            modifier = Modifier.height(70.dp).width(70.dp)
        )
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



