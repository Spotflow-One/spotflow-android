package com.app.spotflowapp.ui.views

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
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PaymentOptionTile
import com.app.spotflowapp.ui.utils.PciDssIcon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.app.spotflowapp.R
import com.app.spotflowapp.core.ApiClient
import com.app.spotflowapp.core.PaymentApi
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.ui.utils.CancelButton
import com.app.spotflowapp.ui.utils.ChangePaymentButton
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import java.util.*


@Composable
fun TransferStatusCheckPage(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    onSuccessfulPayment: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
) {
    var remainingTime by remember { mutableStateOf("10:00") }
    val coroutineScope = rememberCoroutineScope()
    var timerJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {

        coroutineScope.launch {
            startPolling(
                onSuccessfulPayment = onSuccessfulPayment,
                paymentResponseBody = paymentResponseBody,
                paymentManager = paymentManager
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
            title = "Pay with Transfer",
            imageRes = R.drawable.pay_with_transfer_icon // Replace with actual resource
        )
        PaymentCard(paymentManager = paymentManager, rate = paymentResponseBody.rate)
        Spacer(modifier = Modifier.height(70.dp))
        Text(
            text = "We’re waiting to confirm your transfer. This can take a few minutes",
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
) {
    val pollingInterval = 2 * 1000L // 2 minutes in milliseconds
    verifyPayment(
        paymentManager = paymentManager,
        paymentResponseBody = paymentResponseBody,
        onSuccessfulPayment = onSuccessfulPayment
    )// seconds
    while (true) {
        delay(pollingInterval)
        // Call the function to verify payment
       verifyPayment(
            paymentManager = paymentManager,
            paymentResponseBody = paymentResponseBody,
            onSuccessfulPayment = onSuccessfulPayment
        )

    }
}

fun verifyPayment(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    onSuccessfulPayment: () -> Unit,
) {
    Log.d("verifyPayment", "verifyPayment")
    val authToken = paymentManager.key
    val retrofit = ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(PaymentApi::class.java)

    val call =
        apiService.verifyPayment(
            merchantId = paymentManager.merchantId,
            reference = paymentResponseBody.reference
        )
    // on below line we are executing our method.
    call!!.enqueue(object : Callback<PaymentResponseBody?> {
        override fun onResponse(
            call: Call<PaymentResponseBody?>,
            response: retrofit2.Response<PaymentResponseBody?>
        ) {
            // this method is called when we get response from our api.

            println(response.code())
            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: PaymentResponseBody? = response.body()
                if (model?.status == "successful") {
                    onSuccessfulPayment.invoke()
                }
            }
        }

        override fun onFailure(call: Call<PaymentResponseBody?>, t: Throwable) {

        }
    })
}



