package com.spotflow.ui.views

import com.spotflow.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import retrofit2.Call
import retrofit2.Callback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterCardOtp(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    paymentResponseBody: com.spotflow.models.PaymentResponseBody,
    rate: com.spotflow.models.Rate,
    onCancelButtonClicked: () -> Unit,
    onSuccessfulPayment: (com.spotflow.models.PaymentResponseBody) -> Unit
) {
    var otp by remember { mutableStateOf("") }

    val buttonEnabled = otp.isNotEmpty()


    var loading: MutableState<Boolean> = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PaymentOptionTile(
            title = "Pay with Card", imageRes = R.drawable.pay_with_card_icon
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

            paymentResponseBody.providerMessage?.let {
                Text(
                    text = it,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF55515B),
                    modifier = Modifier.padding(
                        top = 57.dp,
                        bottom = 54.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            }

            Box(modifier = Modifier.padding(16.dp)) {
                TextField(
                    value = otp, onValueChange = { otp = it }, modifier = Modifier.border(
                        1.dp, Color(0xFFCECDD0), shape = RoundedCornerShape(8.dp)
                    ).height(60.dp).fillMaxWidth(),


                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent


                    ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }



            Button(
                onClick = {
                    submitCardOtp(
                        paymentManager = paymentManager,
                        paymentResponseBody = paymentResponseBody,
                        otp = otp,
                        loading = loading,
                        onSuccessfulPayment = onSuccessfulPayment,
                    )
                },
                enabled = buttonEnabled,
                modifier = Modifier.padding(horizontal = 90.dp).padding(top = 31.dp).fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF32BB78))
            ) {
                Text(
                    text = "Authorize",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier.padding(vertical = 10.dp)
                )
            }


            Spacer(modifier = Modifier.height(64.dp))
            Button(
                onClick = { onCancelButtonClicked.invoke() },
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = "Cancel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF3D3844)
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
            PciDssIcon()


            Spacer(modifier = Modifier.height(64.dp))
        }


    }

}


private fun submitCardOtp(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    otp: String,
    paymentResponseBody: com.spotflow.models.PaymentResponseBody,
    onSuccessfulPayment: (paymentResponse: com.spotflow.models.PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)


    val validatePaymentRequestBody = com.spotflow.models.ValidatePaymentRequestBody(
        merchantId = paymentManager.merchantId,
        reference = paymentResponseBody.reference,
        authorization = com.spotflow.models.ValidatePaymentAuthorization(otp = otp)
    )

    loading.value = true
    val call = apiService.validatePayment(validatePaymentRequestBody)
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

