package com.spotflow.ui.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.spotflow.R
import com.spotflow.models.PaymentOptionsEnum
import com.spotflow.models.PaymentResponseBody
import com.spotflow.models.Rate
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.ChangePaymentButton
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import java.util.Locale


@Composable
fun TransferStatusCheckPage(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    rate: Rate,
    onSuccessfulPayment: () -> Unit,
    onFailedPayment: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    paymentOptionsEnum: PaymentOptionsEnum,
) {
    var remainingTime by remember { mutableStateOf("10:00") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            startPolling(
                onSuccessfulPayment = onSuccessfulPayment,
                paymentResponseBody = paymentResponseBody,
                paymentManager = paymentManager,
                onFailedPayment = onFailedPayment
            )
        }

        coroutineScope.launch {
            startCountDown { time ->
                remainingTime = time
            }
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PaymentOptionTile(
            title = paymentOptionsEnum.title,
            imageRes = paymentOptionsEnum.icon
        )
        PaymentCard(
            paymentManager = paymentManager,
            rate = rate,
            amount = paymentResponseBody.amount
        )
        Spacer(modifier = Modifier.height(70.dp))
        val paymentText = if (paymentOptionsEnum != PaymentOptionsEnum.TRANSFER) "payment" else ""

        Text(
            text = "We’re waiting to confirm your ${paymentOptionsEnum.name.lowercase(Locale.getDefault())} ${paymentText}. This can take a few minutes",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF55515B),
            modifier = Modifier.padding(horizontal = 30.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.check_icon), // Replace with actual resource
                    contentDescription = null,
                    tint = Color(0xFF32BB78),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Sent",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9BA1)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            LinearProgressIndicator(
                modifier = Modifier
                    .weight(1f)
                    .height(7.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0xFFE6E6E7), RoundedCornerShape(4.dp)),
                color = Color(0xFF32BB78)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(R.drawable.dotted_circle),// Replace with actual resource
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFB6B4B9)
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Received",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9BA1)
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { /* Handle wait button click */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Please wait for $remainingTime minutes",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B),
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (paymentOptionsEnum == PaymentOptionsEnum.TRANSFER) {
            Button(
                onClick = { /* Navigate to ViewBankDetailsPage */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Show account number",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF55515B),
                    modifier = Modifier.padding(vertical = 10.dp)


                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CancelButton(
                onTap = onCancelButtonClicked
            )
            Spacer(modifier = Modifier.width(16.dp))
            ChangePaymentButton(
                onTap = onChangePaymentClicked
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PciDssIcon()

        Spacer(modifier = Modifier.height(64.dp))
    }
}

suspend fun startCountDown(onTimeUpdate: (String) -> Unit) {
    val startTime = 10 * 60 * 1000L // 10 minutes in milliseconds
    val endTime = 0L
    val timeDiff = startTime - endTime

    val startMillis = System.currentTimeMillis()
    while (true) {
        val elapsedMillis = System.currentTimeMillis() - startMillis
        val remainingMillis = timeDiff - elapsedMillis
        if (remainingMillis <= 0) {
            onTimeUpdate("00:00")
            break
        } else {
            val remainingSeconds = (remainingMillis / 1000).toInt()
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            onTimeUpdate(
                "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
            )
            delay(1000L)
        }
    }
}

suspend fun startPolling(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    onSuccessfulPayment: () -> Unit,
    onFailedPayment: () -> Unit,

    ) {
    val pollingInterval = 2 * 1000L // 2 minutes in milliseconds
    verifyPayment(
        paymentManager = paymentManager,
        paymentResponseBody = paymentResponseBody,
        onSuccessfulPayment = onSuccessfulPayment,
        onFailedPayment = onFailedPayment,
    )// seconds
    while (true) {
        delay(pollingInterval)
        // Call the function to verify payment
        verifyPayment(
            paymentManager = paymentManager,
            paymentResponseBody = paymentResponseBody,
            onSuccessfulPayment = onSuccessfulPayment,
            onFailedPayment = onFailedPayment
        )
    }
}

fun verifyPayment(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    onSuccessfulPayment: () -> Unit,
    onFailedPayment: () -> Unit,
) {
    Log.d("verifyPayment", "verifyPayment")
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)

    val call =
        apiService.verifyPayment(
            reference = paymentResponseBody.reference
        )
    // on below line we are executing our method.
    call!!.enqueue(object : Callback<PaymentResponseBody?> {
        override fun onResponse(
            call: Call<PaymentResponseBody?>,
            response: retrofit2.Response<PaymentResponseBody?>
        ) {
            // this method is called when we get response from our api.

            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: PaymentResponseBody? = response.body()
                if (model?.status == "successful") {
                    onSuccessfulPayment.invoke()
                } else if (model?.status == "failed") {
                    onFailedPayment.invoke()
                }
            }
        }

        override fun onFailure(call: Call<PaymentResponseBody?>, t: Throwable) {
            onFailedPayment.invoke()
        }
    })
}



