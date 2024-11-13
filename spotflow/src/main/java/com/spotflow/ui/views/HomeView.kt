package com.spotflow.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.R
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import retrofit2.Call
import retrofit2.Callback
import java.text.NumberFormat
import java.util.Locale


@Composable
fun HomeView(
    paymentManager: SpotFlowPaymentManager,
    onPayWithCardClicked: () -> Unit,
    onPayWithTransferClicked: () -> Unit,
    onPayWithUSSDClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    onRateFetched: (rate: com.spotflow.models.MerchantConfig) -> Unit
) {

    val merchantConfig: MutableState<com.spotflow.models.MerchantConfig?> =
        remember { mutableStateOf(null) }
    val loading: MutableState<Boolean> = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getRate(
            result = merchantConfig,
            paymentManager = paymentManager,
            loading = loading,
            onRateFetched = onRateFetched
        )
    }


    if (loading.value || merchantConfig.value == null) {
        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp),
            contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier.height(50.dp)

            )
        }

    } else {
        val numberFormatter: NumberFormat = NumberFormat.getInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            horizontalAlignment = Alignment.Start,

            ) {

            PaymentCard(
                paymentManager = paymentManager,
                rate = merchantConfig.value!!.rate,
                amount = merchantConfig.value!!.plan.amount
            )

            merchantConfig.value?.rate?.let {
                val amount = it.rate * merchantConfig.value!!.plan.amount.toDouble()
                val formattedAmount = numberFormatter.format(amount)
                Text(
                    text = "Use one of the payment methods below to pay ${it.from} $formattedAmount to Spotflow",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9E9BA1),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(vertical = 27.dp, horizontal = 12.dp)
                )

            }

            Text(
                text = "Payment Options",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6D6A73),
                modifier = Modifier
                    .padding(top = 33.dp, start = 41.dp)
            )
            Divider(
                color = Color(0xFFE6E6E7),
                thickness = 0.5.dp,
                modifier = Modifier
                    .padding(horizontal = 17.dp)
                    .padding(top = 16.5.dp, bottom = 8.dp)
            )

            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                // Replace with your actual navigation logic
                PaymentOptionTile(
                    title = "Pay with Card",
                    imageRes = R.drawable.pay_with_card_icon,
                    onTap = onPayWithCardClicked
                )

                PaymentOptionTile(
                    title = "Pay with Transfer",
                    imageRes = R.drawable.pay_with_transfer_icon,
                    onTap = onPayWithTransferClicked

                )

                PaymentOptionTile(
                    title = "Pay with USSD",
                    imageRes = R.drawable.ussd_icon,
                    onTap = onPayWithUSSDClicked
                )
            }
            Spacer(modifier = Modifier.weight(2f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(1f))
                CancelButton(
                    onTap = onCancelButtonClicked
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.weight(1f))
            PciDssIcon()
            Spacer(modifier = Modifier.weight(1f))
        }
    }


}

private fun getRate(
    result: MutableState<com.spotflow.models.MerchantConfig?>,
    paymentManager: SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    onRateFetched: (rate: com.spotflow.models.MerchantConfig) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)

    loading.value = true
    val call =
        apiService.getMerchantConfig(planId = paymentManager.planId)


    // on below line we are executing our method.
    call!!.enqueue(object : Callback<com.spotflow.models.MerchantConfig?> {
        override fun onResponse(
            call: Call<com.spotflow.models.MerchantConfig?>,
            response: retrofit2.Response<com.spotflow.models.MerchantConfig?>
        ) {
            // this method is called when we get response from our api.

            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: com.spotflow.models.MerchantConfig? = response.body()
                if (model != null) {
                    result.value = model
                    onRateFetched.invoke(model)
                }
            }
            loading.value = false
        }

        override fun onFailure(call: Call<com.spotflow.models.MerchantConfig?>, t: Throwable) {
            loading.value = false

        }
    })
}


