package com.spotflow.models
import java.util.Date

data class PaymentResponseBody(
    val id: String,
    val reference: String,
    val spotflowReference: String,
    val amount: Double,
    val currency: String,
    val channel: String,
    val status: String,
    val customer: CustomerInfo,
    val provider: String,
    val rate: Double?,
    val authorization: Authorization?,
    val createdAt: Date,
    val providerMessage: String?,
    val bankDetails: BankDetails?
)

data class Rate(
    val from: String,
    val to: String,
    val rate: Double
)


data class Authorization(
    val mode: String,
    val redirectUrl: String?
)

data class BankDetails(
    val accountNumber: String,
    val bankName: String,
    val expiresAt: Date = Date(System.currentTimeMillis() + 30 * 60 * 1000)
)




