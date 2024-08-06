package com.app.spotflowapp.ui.utils

import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.spotflowapp.models.SpotFlowPaymentManager
import java.text.NumberFormat
import java.util.*

@Composable
fun PaymentCard(paymentManager: SpotFlowPaymentManager, rate: Rate?) {
    val numberFormatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp)
            .padding(top = 16.dp)
            .background(Color(0xFF01008E), shape = RoundedCornerShape(10.dp))
            .padding(vertical = 15.dp)
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
                rate?.let {
                    Text(
                        text = "${it.from} 1 = ${it.to} ${numberFormatter.format(it.rate)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.info_circle),
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Pay ",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
                val formattedValue = numberFormatter.format(paymentManager.amount)
                Text(
                    text = "${paymentManager.fromCurrency} $formattedValue",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            rate?.let {
                val amount = it.rate * paymentManager.amount
                val formattedAmount = numberFormatter.format(amount)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFF32BB78), shape = RoundedCornerShape(4.dp))
                        .padding(vertical = 4.dp, horizontal = 10.dp)
                ) {
                    Text(
                        text = "${it.from} $formattedAmount",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }
            } ?: Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
