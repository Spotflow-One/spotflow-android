package com.spotflow.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.* // For Column, Row, etc.
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.text.style.TextAlign
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import com.spotflow.R


@Composable
fun TransferInfoView(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    onKeepWaitingClicked: () -> Unit,
    rate: com.spotflow.models.Rate,
    amount: Number
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    val copyTexts = listOf(
        "We’ll complete this transaction automatically once we confirm your transfer.",
        "If you have any issues with this transfer, please contact us via support@spotflow.com"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        PaymentOptionTile(
            title = "Pay with Transfer",
            imageRes = R.drawable.pay_with_transfer_icon // Replace with your actual resource
        )

        PaymentCard(paymentManager = paymentManager, rate = rate, amount = amount)

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = copyTexts[currentIndex],
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF55515B),
            modifier = Modifier.padding(horizontal = 50.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Indicator(index = 0, currentIndex = currentIndex)
            Spacer(modifier = Modifier.width(8.dp))
            Indicator(index = 1, currentIndex = currentIndex)
        }

        Spacer(modifier = Modifier.height(60.dp))

        if (currentIndex == 0) {
            Button(
                onClick = { currentIndex = 1 },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)

            ) {
                Text(
                    text = "Next",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = Color(0xFF55515B)

                )
            }
            Spacer(modifier = Modifier.height(70.dp))
        } else {
            Column {
                Button(
                    onClick = { onCancelButtonClicked.invoke() },
                    shape = RoundedCornerShape(8.dp),

                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32BB78)),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
                ) {
                    Text(
                        text = "Close Checkout",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 10.dp),

                        )
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    onClick = { onKeepWaitingClicked.invoke()},
                    shape = RoundedCornerShape(8.dp),

                    modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
                ) {
                    Text(
                        text = "Keep waiting",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFF55515B)
                    )
                }
                Spacer(modifier = Modifier.height(70.dp))
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(18.dp, Alignment.CenterHorizontally)
        ) {
            ChangePaymentButton(
                onTap = onChangePaymentClicked
            )
            CancelButton(
                onTap = onCancelButtonClicked
            )
        }

        Spacer(modifier = Modifier.height(64.dp))

        PciDssIcon()

        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
fun Indicator(index: Int, currentIndex: Int) {
    Box(
        modifier = Modifier.size(12.dp).clip(CircleShape).border(
                width = 1.dp,
                color = if (currentIndex == index) Color.Transparent else Color(0xFFC0B5CF),
                shape = CircleShape
            ).background(
                if (currentIndex == index) Color(0xFF01008E) else Color.Transparent,
                shape = CircleShape
            )

    )
}



