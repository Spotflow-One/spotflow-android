package com.app.spotflowapp.ui.views
import android.view.Display.Mode
import androidx.compose.foundation.BorderStroke
import com.app.spotflowapp.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.spotflowapp.core.ApiClient
import com.app.spotflowapp.core.PaymentApi
import com.app.spotflowapp.models.PaymentRequestBody
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.SpotFlowCard
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.ui.utils.CancelButton
import com.app.spotflowapp.ui.utils.CardTextField
import com.app.spotflowapp.ui.utils.ChangePaymentButton
import com.app.spotflowapp.ui.utils.PaymentCard
import com.app.spotflowapp.ui.utils.PaymentOptionTile
import com.app.spotflowapp.ui.utils.PciDssIcon
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

@Composable
fun EnterCardDetail(
    paymentManager: SpotFlowPaymentManager,
    rate: Rate? = null,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    onSuccessfulPayment: (PaymentResponseBody) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    var loading: MutableState<Boolean> = remember { mutableStateOf(false) }

    val buttonEnabled = cardCvv.isNotEmpty() && cardExpiry.isNotEmpty() && cardNumber.isNotEmpty()


    Column(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)) {
        // PaymentOptionTile, PaymentCard and other custom composables to be implemented
        PaymentOptionTile(title = "Pay with Card", imageRes = R.drawable.pay_with_card_icon)
        PaymentCard(paymentManager = paymentManager, rate = rate)

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
                text = "Enter your card details to pay",
                modifier = Modifier.padding(top = 34.dp, bottom = 24.dp).fillMaxWidth(),
                color = Color(0xFF55515B),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            CardTextField(
                value = cardNumber,
                label = "CARD NUMBER",
                hintText = "0000 0000 0000 0000",
                onValueChange = { cardNumber = it },
                visualTransformation = CardNumberVisualTransformation(),
                modifier =   Modifier.padding(horizontal = 16.dp, vertical = 4.5.dp)
                    .border(
                        BorderStroke(1.dp, Color(0xFFE6E6E7)),
                        shape = RoundedCornerShape(5.dp)
                    ).fillMaxWidth()
            )



            Row(modifier = Modifier.padding(top = 14.dp)) {
                Spacer(modifier = Modifier.width(16.dp))
                CardTextField(
                    value = cardExpiry,
                    label = "CARD EXPIRY",
                    hintText = "MM/YY",
                    onValueChange = { cardExpiry = it },
                    visualTransformation = ExpiryDateVisualTransformation(),
                    modifier = Modifier.padding(vertical = 4.5.dp)
                        .border(
                            BorderStroke(1.dp, Color(0xFFE6E6E7)),
                            shape = RoundedCornerShape(5.dp)
                        ).weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                CardTextField(
                    value = cardCvv,
                    label = "CVV",
                    hintText = "123",
                    onValueChange = { cardCvv = it },
                    visualTransformation = CvvVisualTransformation(),
                    modifier = Modifier.padding( vertical = 4.5.dp)
                        .border(
                            BorderStroke(1.dp, Color(0xFFE6E6E7)),
                            shape = RoundedCornerShape(5.dp)
                        ).weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))

            }

            Button(
                onClick = {
                    val expiryDate = extractExpiryDate(cardExpiry)
                    if(expiryDate != null) {
                        submitCardDetails(
                            loading = loading,
                            paymentManager = paymentManager,
                            onSuccessfulPayment = onSuccessfulPayment,
                            spotFlowCard = SpotFlowCard(
                                pan = cardNumber,
                                cvv = cardCvv,
                                expiryYear = expiryDate.year,
                                expiryMonth = expiryDate.month
                            )
                        )
                    }

                },
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                enabled = buttonEnabled,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF01008E),
                    disabledContainerColor = Color(0xFFF4F4FF)
                )
            ) {
                Text("Pay USD 14.99",
                    color = if (buttonEnabled) Color.White else Color(0xFFAAAAD9),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }

            Spacer(modifier = Modifier.height(12.dp))

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
            Spacer(modifier = Modifier.weight(1f))

            PciDssIcon()
            Spacer(modifier = Modifier.height(64.dp))
        }

    }
}

private fun submitCardDetails(
    paymentManager: SpotFlowPaymentManager,
    loading: MutableState<Boolean>,
    spotFlowCard: SpotFlowCard,
    onSuccessfulPayment: (paymentResponse: PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(PaymentApi::class.java)


    val paymentRequestBody = PaymentRequestBody(
        customer = paymentManager.customer,
        currency = paymentManager.fromCurrency,
        amount = paymentManager.amount,
        channel = "card",
        card = spotFlowCard,
        provider = paymentManager.provider
    )

    loading.value = true
    val call =
        apiService.createPayment(paymentRequestBody)
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


fun extractExpiryDate(dateString: String): ExpiryDate? {
    val components = dateString.split("/")
    return if (components.size == 2) {
        val month = components[0]
        val year = components[1]
        ExpiryDate(month, year)
    } else {
        null
    }
}

data class ExpiryDate(val month: String, val year: String)


class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }

        val numberOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }

        return TransformedText(AnnotatedString(out), numberOffsetTranslator)
    }
}

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0..3) else text.text
        val out = StringBuilder()
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if (i == 1) out.append("/")
        }

        val expiryOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }

        return TransformedText(AnnotatedString(out.toString()), expiryOffsetTranslator)
    }
}


class CvvVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val masked = "*".repeat(text.text.length)
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset
            override fun transformedToOriginal(offset: Int): Int = offset
        }

        return TransformedText(AnnotatedString(masked), offsetMapping)
    }
}
