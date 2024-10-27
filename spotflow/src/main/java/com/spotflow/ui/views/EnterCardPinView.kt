package com.spotflow.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.R
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import com.spotflow.ui.utils.PinInputView
import retrofit2.Call
import retrofit2.Callback

@Composable
fun EnterCardPin(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    paymentResponseBody: com.spotflow.models.PaymentResponseBody,
    rate: com.spotflow.models.Rate,
    onCancelButtonClicked: () -> Unit,
    onSuccessfulPayment: (com.spotflow.models.PaymentResponseBody) -> Unit
) {
    var loading: MutableState<Boolean> = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentOptionTile(
            title = "Pay with Card",
            imageRes = R.drawable.pay_with_card_icon
        )

        PaymentCard(
            paymentManager = paymentManager,
            rate = rate,
            amount = paymentResponseBody.amount,
        )

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
            Text(
                text = "Please enter your 4-digit card pin to authorize this payment",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF55515B),
                modifier = Modifier.padding(
                    top = 34.dp,
                    bottom = 34.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
                textAlign = TextAlign.Center
            )

            PinInputView(onComplete = {
                submitCardPin(
                    paymentManager = paymentManager,
                    loading = loading,
                    pin = it,
                    paymentResponseBody = paymentResponseBody,
                    onSuccessfulPayment = onSuccessfulPayment,
                )
            })

            Spacer(
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = { onCancelButtonClicked.invoke() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 121.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF3D3844)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            PciDssIcon()
            Spacer(modifier = Modifier.height(64.dp))
        }


    }
}

private fun submitCardPin(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    pin: String,
    paymentResponseBody: com.spotflow.models.PaymentResponseBody,
    onSuccessfulPayment: (paymentResponse: com.spotflow.models.PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)


    val authorizePaymentRequestBody = com.spotflow.models.AuthorizePaymentRequestBody(
        authorization = com.spotflow.models.AuthorizePaymentAuthorization(
            pin = pin,
        ),
        merchantId = paymentManager.merchantId,
        reference = paymentResponseBody.reference
    )


    5531886652142950
    loading.value = true
    val call =
        apiService.authorizePayment(authorizePaymentRequestBody)
    // on below line we are executing our method.
    call!!.enqueue(object : Callback<com.spotflow.models.PaymentResponseBody?> {
        override fun onResponse(
            call: Call<com.spotflow.models.PaymentResponseBody?>,
            response: retrofit2.Response<com.spotflow.models.PaymentResponseBody?>
        ) {
            // this method is called when we get response from our api.

            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: com.spotflow.models.PaymentResponseBody? = response.body()
                if (model != null) {
                    onSuccessfulPayment.invoke(model)
                }
            }
            loading.value = false
        }

        override fun onFailure(call: Call<com.spotflow.models.PaymentResponseBody?>, t: Throwable) {
            loading.value = false

        }
    })
}


