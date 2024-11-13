package com.spotflow.ui.views


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.* // For Column, Row, etc.
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.ui.utils.BankAccountCard
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import kotlinx.coroutines.delay
import com.spotflow.R
import retrofit2.Call
import retrofit2.Callback
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.max

@Composable
fun TransferHomeView(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    merchantConfig: com.spotflow.models.MerchantConfig,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    onIveSentTheMoneyClicked: () -> Unit,
    onCreatedPayment: (paymentResponse: com.spotflow.models.PaymentResponseBody) -> Unit
) {


    val paymentResponse: MutableState<com.spotflow.models.PaymentResponseBody?> =
        remember { mutableStateOf(null) }
    val loading: MutableState<Boolean> = remember { mutableStateOf(false) }

    val amount = merchantConfig.plan.amount.toDouble()
    val rate = merchantConfig.rate
    var formattedAmount: String? = null
    var converteedAmount: Double? = null
    val numberFormatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }
    converteedAmount = rate.rate * amount
    formattedAmount =
        "${rate.to} ${numberFormatter.format(amount)}"

    LaunchedEffect(Unit) {
        createPayment(
            result = paymentResponse,
            paymentManager = paymentManager,
            loading = loading,
            onPaymentCreated = onCreatedPayment,
            rate = rate,
            amount = amount
        )
    }

    if (loading.value) {
        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier.height(50.dp)

            )
        }

    } else {
        if (paymentResponse.value?.status == "failed") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                PaymentOptionTile(
                    title = "Pay with Transfer",
                    imageRes = R.drawable.pay_with_transfer_icon // Replace with your actual resource
                )
                PaymentCard(paymentManager = paymentManager, rate = rate, amount = amount)

            }
        } else if (paymentResponse.value != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp)
            ) {
                Spacer(
                    modifier = Modifier.height(16.dp)
                )
                PaymentOptionTile(
                    title = "Pay with Transfer",
                    imageRes = R.drawable.pay_with_transfer_icon // Replace with your actual resource
                )

                PaymentCard(paymentManager = paymentManager, rate = rate, amount = amount)

                paymentResponse.value?.rate?.let {
                    val rateAmount = it * amount
                    val formattedRateAmount = numberFormatter.format(rateAmount)
                    Text(
                        text = "Transfer ${rate.from} $formattedRateAmount to the details below",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF55515B),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
                    )
                } ?: Spacer(modifier = Modifier.height(50.dp))

                Spacer(
                    modifier = Modifier.padding(bottom = 19.dp)
                )
                BankAccountCard(
                    bankDetails = paymentResponse.value!!.bankDetails!!,
                    formattedAmount = formattedAmount,
                    amount = amount
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                CountdownTimer(totalDurationMillis = 30 * 60 * 1000)

                Button(
                    onClick = onIveSentTheMoneyClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                ) {
                    Text(
                        text = "I've sent the money",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF55515B),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

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
                Spacer(modifier = Modifier.height(64.dp))
                Spacer(modifier = Modifier.weight(1f))
                PciDssIcon()
                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }


}


@Composable
fun CircularDeterminateIndicator(currentProgress: Float) {

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(45.dp),
            color = Color(0xFF01008E),
            trackColor = Color(0xFFE1E0F1),
            progress = currentProgress
        )
    }
}


private fun createPayment(
    result: MutableState<com.spotflow.models.PaymentResponseBody?>,
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    onPaymentCreated: (paymentResponse: com.spotflow.models.PaymentResponseBody) -> Unit,
    rate: com.spotflow.models.Rate,
    amount: Double

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)


    val paymentRequestBody = com.spotflow.models.PaymentRequestBody(
        customer = paymentManager.customer,
        currency = rate.from,
        amount = amount,
        channel = "bank_transfer",
    )

    loading.value = true
    val call =
        apiService.createPayment(paymentRequestBody)
    // on below line we are executing our method.
    call!!.enqueue(object : Callback<com.spotflow.models.PaymentResponseBody?> {
        override fun onResponse(
            call: Call<com.spotflow.models.PaymentResponseBody?>,
            response: retrofit2.Response<com.spotflow.models.PaymentResponseBody?>
        ) {
            // this method is called when we get response from our api.

            println(response.code())
            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: com.spotflow.models.PaymentResponseBody? = response.body()
                if (model != null) {
                    result.value = model
                    onPaymentCreated.invoke(model)
                }
            }
            loading.value = false
        }

        override fun onFailure(call: Call<com.spotflow.models.PaymentResponseBody?>, t: Throwable) {
            loading.value = false

        }
    })
}


@Composable
fun CountdownTimer(totalDurationMillis: Long) {
    var remainingTimeMillis by remember { mutableStateOf(totalDurationMillis) }

    // Timer logic to update remaining time
    LaunchedEffect(Unit) {
        while (remainingTimeMillis > 0) {
            delay(1000L)
            remainingTimeMillis = max(remainingTimeMillis - 1000L, 0L)
        }
    }

    // Map remaining time to a value between 0 and 1
    val progress by animateFloatAsState(
        targetValue = remainingTimeMillis / totalDurationMillis.toFloat(),
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearEasing
        )
    )

    val remainingSeconds = (remainingTimeMillis / 1000).toInt()
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val time = "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"

    Box {
        Icon(
            painter = painterResource(id = R.drawable.pay_with_transfer_icon), // Replace with your actual resource
            contentDescription = null,
            tint = Color(0xFF32BB78),
            modifier = Modifier.align(Alignment.Center).size(15.dp)
        )

        CircularDeterminateIndicator(progress)

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Expires in ",
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFB6B4B9)
        )
        Text(
            text = time,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF32BB78)
        )
    }

}





