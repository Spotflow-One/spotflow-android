package com.spotflow.ui.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PciDssIcon
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.R
import com.spotflow.models.Bank
import com.spotflow.models.BankRequestBody
import com.spotflow.models.MerchantConfig
import com.spotflow.models.PaymentRequestBody
import com.spotflow.models.PaymentResponseBody
import com.spotflow.models.Rate
import com.spotflow.models.SpotFlowPaymentManager
import retrofit2.Call
import retrofit2.Callback


@Composable
fun CopyUssdCodePage(
    paymentManager: SpotFlowPaymentManager,
    rate: Rate,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    onConfirmPayment: (paymentResponse: PaymentResponseBody) -> Unit,
    merchantConfig: MerchantConfig,

    bank: Bank,
) {

    var currentIndex by remember { mutableIntStateOf(0) }

    val copyTexts = listOf(
        "Dial the code below to complete this transaction with ${bank.name}",
        "Dial the payment code below to complete this transaction with ${bank.name}"
    )

    val loading: MutableState<Boolean> = remember { mutableStateOf(false) }
    val paymentResponseBody: MutableState<PaymentResponseBody?> = remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        createPayment(
            paymentManager = paymentManager,
            loading = loading,
            merchantConfig = merchantConfig,
            bankCode = bank.code,
            paymentResponse = paymentResponseBody,
        )
    }

    if (loading.value || paymentResponseBody.value == null) {
        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.height(50.dp)
            )
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
        ) {

            PaymentOptionTile(
                imageRes = R.drawable.ussd_icon, // Replace with appropriate drawable resource
                title = "Pay with USSD"
            )

            PaymentCard(
                paymentManager = paymentManager,
                rate = rate,
                amount = paymentResponseBody.value!!.amount,
            )

            Spacer(modifier = Modifier.height(70.dp))

            Icon(
                painter = painterResource(id = R.drawable.ussd_icon), // Replace with appropriate drawable resource
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                tint = Color(0xFF01008E)
            )

            Spacer(modifier = Modifier.height(9.dp))

            Text(
                text = copyTexts[currentIndex],
                style = TextStyle(
                    fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55515B)
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 16.dp),
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (currentIndex == 0) {
                        currentIndex = 1
                    } else {
                        onConfirmPayment.invoke(paymentResponseBody.value!!)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF01008E)
                ),
                modifier =
                Modifier.fillMaxWidth().padding(horizontal = 60.dp)
            ) {
                if (currentIndex == 0) {
                    Text(
                        text = paymentResponseBody.value!!.ussd?.code ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFFF4F4FF)
                    )
                } else {
                    Text(
                        text = paymentResponseBody.value!!.ussd?.paymentCode ?: "",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFFF4F4FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(19.dp))

            Text(
                text = "Click to copy",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                color = Color(0xFF86838A)
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    if (currentIndex == 0) {
                        currentIndex = 1
                    } else {
                        onConfirmPayment.invoke(paymentResponseBody.value!!)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, Color(0xFFC0B5CF)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp)
            ) {
                if (currentIndex == 0) {
                    Text(
                        text = "Next",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFF55515B)

                    )
                } else {
                    Text(
                        text = "I have completed the payment",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 10.dp),
                        color = Color(0xFF55515B)

                    )
                }
            }
            Spacer(modifier = Modifier.height(70.dp))
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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
            Spacer(modifier = Modifier.height(38.dp))
        }
    }

}

@Composable
fun BankSelectionTile(bankName: String, ussdCode: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp).border(
            width = 1.dp, color = Color(0xFFC0B5CF), shape = RoundedCornerShape(8.dp)
        ).padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = bankName, style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.background(
                color = Color(0xFFCCCCE8), shape = RoundedCornerShape(6.dp)
            ).padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            Text(
                text = ussdCode, style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}


private fun createPayment(
    paymentManager: SpotFlowPaymentManager,
    merchantConfig: MerchantConfig,
    bankCode: String,
    loading: MutableState<Boolean>,
    paymentResponse: MutableState<PaymentResponseBody?>,
) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)

    val paymentRequestBody = PaymentRequestBody(
        customer = paymentManager.customer,
        currency = merchantConfig.rate.to,
        amount = merchantConfig.plan.amount.toDouble(),
        channel = "ussd",
        bank = BankRequestBody(code = bankCode)
    )

    loading.value = true
    val call = apiService.createPayment(paymentRequestBody)

    call!!.enqueue(object : Callback<PaymentResponseBody?> {
        override fun onResponse(
            call: Call<PaymentResponseBody?>, response: retrofit2.Response<PaymentResponseBody?>
        ) {

            if (response.code() == 200) {
                paymentResponse.value = response.body()
            }
            loading.value = false
        }

        override fun onFailure(call: Call<PaymentResponseBody?>, t: Throwable) {
            loading.value = false

        }
    })
}
