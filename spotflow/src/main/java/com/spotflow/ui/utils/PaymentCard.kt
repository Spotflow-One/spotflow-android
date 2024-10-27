package com.spotflow.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.models.Rate
import com.spotflow.models.SpotFlowPaymentManager
import java.text.NumberFormat
import java.util.*


@Composable
fun PaymentCard(
    paymentManager: SpotFlowPaymentManager,
    rate: Rate,
    amount: Number
) {
    val numberFormatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 16.dp)
            .background(Color(0xFF01008E), shape = RoundedCornerShape(10.dp))
            .padding(bottom = 31.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 48.dp)
            ) {
                Text(
                    text = paymentManager.customerEmail,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = paymentManager.paymentDescription ?: "",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }

            Divider(
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, bottom = 6.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Pay ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                val formattedValue = numberFormatter.format(amount)
                Text(
                    text = "${rate.to} $formattedValue",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }


        }
    }
}
