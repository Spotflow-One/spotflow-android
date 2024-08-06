package com.app.spotflowapp.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.R
import com.app.spotflowapp.core.ApiClient
import com.app.spotflowapp.core.PaymentApi
import com.app.spotflowapp.models.Authorization
import com.app.spotflowapp.models.AuthorizePaymentAuthorization
import com.app.spotflowapp.models.AuthorizePaymentRequestBody
import com.app.spotflowapp.models.ValidatePaymentRequestBody
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PaymentOptionTile
import com.app.spotflowapp.ui.utils.PciDssIcon
import com.app.spotflowapp.ui.utils.PinInputView
import retrofit2.Call
import retrofit2.Callback

@Composable
fun EnterCardPin(
    paymentManager: SpotFlowPaymentManager,
    paymentResponseBody: PaymentResponseBody,
    onCancelButtonClicked: () -> Unit,
    onSuccessfulPayment: (PaymentResponseBody) -> Unit
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

        PaymentCard(paymentManager = paymentManager, rate = paymentResponseBody.rate)

        if(loading.value) {
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
                modifier = Modifier.padding(top = 34.dp, bottom = 34.dp, start = 16.dp, end = 16.dp),
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
    paymentManager: SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    pin: String,
    paymentResponseBody: PaymentResponseBody,
    onSuccessfulPayment: (paymentResponse: PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(PaymentApi::class.java)


    val authorizePaymentRequestBody = AuthorizePaymentRequestBody(
        authorization = AuthorizePaymentAuthorization(
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
                if (model != null) {
                    onSuccessfulPayment.invoke(model)
                }
            }
            loading.value = false
        }

        override fun onFailure(call: Call<PaymentResponseBody?>, t: Throwable) {
            loading.value = false

        }
    })
}


