package com.app.spotflowapp.models

import kotlinx.serialization.Serializable
import java.util.UUID



data class ValidatePaymentRequestBody(
    val authorization: ValidatePaymentAuthorization,
    val reference: String,
    val merchantId: String
)

data class AuthorizePaymentRequestBody(
    val authorization: AuthorizePaymentAuthorization,
    val reference: String,
    val merchantId: String

)

data class AuthorizePaymentAuthorization (
    val pin: String
)

data class ValidatePaymentAuthorization (
    val otp: String
)

data class PaymentRequestBody(
    val reference: String = generateReference(),
    val amount: Double,
    val currency: String,
    val channel: String,
    val card: SpotFlowCard? = null,
    val planId: String? = null,
    val provider: String,
    val customer: CustomerInfo
) {
    companion object {
        private fun generateReference(): String {
            return "ref-${UUID.randomUUID()}"
        }
    }

}


data class SpotFlowCard(
    val pan: String,
    val cvv: String,
    val expiryYear: String,
    val expiryMonth: String
)


