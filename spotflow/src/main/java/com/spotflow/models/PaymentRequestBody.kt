package com.spotflow.models


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

data class AuthorizePaymentAuthorization(
    val pin: String
)

data class ValidatePaymentAuthorization(
    val otp: String
)

data class PaymentRequestBody(
    val reference: String = generateReference(),
    val amount: Double,
    val currency: String,
    val channel: String,
    val encryptedCard: String? = null,
    val planId: String? = null,
    val customer: CustomerInfo,
    val bank: BankRequestBody? = null
) {
    companion object {
        private fun generateReference(): String {
            return "ref-${UUID.randomUUID()}"
        }
    }

}


data class BankRequestBody (val code: String)

data class AvsPaymentRequestBody(
    val reference: String,
    val authorization: AvsAuthorization,
    val merchantId: String
)

data class Avs(
    val address: String,
    val country: String,
    val state: String,
    val city: String,
    val zipCode: String,
)

data class AvsAuthorization(
    val avs: Avs
)

data class SpotFlowCard(
    val pan: String,
    val cvv: String,
    val expiryYear: String,
    val expiryMonth: String
)





