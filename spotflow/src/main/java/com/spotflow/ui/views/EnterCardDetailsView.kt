package com.spotflow.ui.views

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import com.spotflow.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.models.SpotFlowCard
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.CardTextField
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import java.security.SecureRandom
import java.text.NumberFormat
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Composable
fun EnterCardDetail(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    rate: com.spotflow.models.Rate,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    amount: Number,
    onSuccessfulPayment: (com.spotflow.models.PaymentResponseBody) -> Unit
) {
    var cardNumber by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    val numberFormatter: NumberFormat =
        NumberFormat.getInstance(Locale.getDefault()).apply {
            maximumFractionDigits = 2
            minimumFractionDigits = 2
        }


    var loading: MutableState<Boolean> = remember { mutableStateOf(false) }

    val buttonEnabled = cardCvv.isNotEmpty() && cardExpiry.isNotEmpty() && cardNumber.isNotEmpty()


    Column(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)) {
        // PaymentOptionTile, PaymentCard and other custom composables to be implemented
        PaymentOptionTile(title = "Pay with Card", imageRes = R.drawable.pay_with_card_icon)
        PaymentCard(paymentManager = paymentManager, rate = rate, amount = amount)

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
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.5.dp)
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
                    modifier = Modifier.padding(vertical = 4.5.dp)
                        .border(
                            BorderStroke(1.dp, Color(0xFFE6E6E7)),
                            shape = RoundedCornerShape(5.dp)
                        ).weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))

            }
            val context = LocalContext.current
            Button(
                onClick = {
                    val expiryDate = extractExpiryDate(cardExpiry)
                    if (expiryDate != null) {
                        submitCardDetails(
                            loading = loading,
                            paymentManager = paymentManager,
                            onSuccessfulPayment = onSuccessfulPayment,
                            rate = rate,
                            amount = amount.toDouble(),
                            spotFlowCard = SpotFlowCard(
                                pan = cardNumber,
                                cvv = cardCvv,
                                expiryYear = expiryDate.year,
                                expiryMonth = expiryDate.month
                            )
                        )
                    } else {

                        Toast.makeText(context, "Invalid expiry date", Toast.LENGTH_SHORT).show()
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
                Text(
                    "Pay ${rate.to} ${numberFormatter.format(amount)}",
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
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    rate: com.spotflow.models.Rate,
    loading: MutableState<Boolean>,
    spotFlowCard: SpotFlowCard,
    amount: Double,
    onSuccessfulPayment: (paymentResponse: com.spotflow.models.PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)

    val encryptedCard =
        encryptCard(spotFlowCard = spotFlowCard, encryptionKey = paymentManager.encryptionKey)

    val paymentRequestBody = com.spotflow.models.PaymentRequestBody(
        customer = paymentManager.customer,
        currency = rate.to,
        amount = amount,
        channel = "card",
        encryptedCard = encryptedCard,
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

@OptIn(ExperimentalEncodingApi::class)
fun encryptCard(spotFlowCard: SpotFlowCard, encryptionKey: String): String {
    val keyBytes = Base64.decode(encryptionKey)
    val secretKey = SecretKeySpec(keyBytes, 0, keyBytes.size, "AES")

    val GCM_IV_LENGTH = 12
    val GCM_TAG_LENGTH = 16


    fun encrypt(plainText: String, secretKey: SecretKey): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")

        // Generate a random IV
        val iv = ByteArray(GCM_IV_LENGTH)
        SecureRandom().nextBytes(iv)

        val gcmParameterSpec = GCMParameterSpec(GCM_TAG_LENGTH * 8, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec)

        val cipherText = cipher.doFinal(plainText.toByteArray())

        // Combine IV and cipherText into a single byte array
        val cipherTextWithIv = ByteArray(iv.size + cipherText.size)
        System.arraycopy(iv, 0, cipherTextWithIv, 0, iv.size)
        System.arraycopy(cipherText, 0, cipherTextWithIv, iv.size, cipherText.size)

        // Return the Base64 encoded cipherText
        return Base64.encode(cipherTextWithIv)
    }

    return encrypt(plainText = Gson().toJson(spotFlowCard), secretKey);
}


fun extractExpiryDate(dateString: String): ExpiryDate? {
    println(dateString)

    return if (dateString.trim().length == 4) {
        val month = dateString.substring(0..1)
        val year = dateString.substring(2..3)
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
