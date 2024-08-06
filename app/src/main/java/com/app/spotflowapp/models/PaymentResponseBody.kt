package com.app.spotflowapp.models

import kotlinx.serialization.Serializable
import java.util.Date
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

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
    val rate: Rate?,
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

@Serializable
data class Authorization(
    val mode: String,
    val redirectUrl: String?
)

data class BankDetails(
    val accountNumber: String,
    val bankName: String,
    val expiresAt: Date = Date(System.currentTimeMillis() + 30 * 60 * 1000)
) {
    companion object {
        fun expiresAt(): Date {
            return Date(System.currentTimeMillis() + 30 * 60 * 1000)
        }
    }
}



fun PaymentResponseBody.toJson(): JSONObject {
    val json = JSONObject()
    json.put("id", id)
    json.put("reference", reference)
    json.put("spotflowReference", spotflowReference)
    json.put("amount", amount)
    json.put("currency", currency)
    json.put("channel", channel)
    json.put("status", status)
    json.put("customer", customer.toJson())
    json.put("provider", provider)
    json.put("rate", rate?.toJson())
    json.put("authorization", authorization?.toJson())
    json.put("createdAt", createdAt.toIsoString())
    json.put("providerMessage", providerMessage)
    json.put("bankDetails", bankDetails?.toJson())
    return json
}

fun JSONObject.toPaymentResponseBody(): PaymentResponseBody {
    return PaymentResponseBody(
        id = getString("id"),
        reference = getString("reference"),
        spotflowReference = getString("spotflowReference"),
        amount = getDouble("amount"),
        currency = getString("currency"),
        channel = getString("channel"),
        status = getString("status"),
        customer = getJSONObject("customer").toCustomerInfo(),
        provider = getString("provider"),
        rate = optJSONObject("rate")?.toRate(),
        authorization = optJSONObject("authorization")?.toAuthorization(),
        createdAt = getString("createdAt").toDate(),
        providerMessage = optString("providerMessage", null),
        bankDetails = optJSONObject("bankDetails")?.toBankDetails()
    )
}

fun Rate.toJson(): JSONObject {
    val json = JSONObject()
    json.put("from", from)
    json.put("to", to)
    json.put("rate", rate)
    return json
}

fun JSONObject.toRate(): Rate {
    return Rate(
        from = getString("from"),
        to = getString("to"),
        rate = getDouble("rate")
    )
}

fun Authorization.toJson(): JSONObject {
    val json = JSONObject()
    json.put("mode", mode)
    json.put("redirectUrl", redirectUrl)
    return json
}

fun JSONObject.toAuthorization(): Authorization {
    return Authorization(
        mode = getString("mode"),
        redirectUrl = optString("redirectUrl", null)
    )
}

fun BankDetails.toJson(): JSONObject {
    val json = JSONObject()
    json.put("accountNumber", accountNumber)
    json.put("name", bankName)
    json.put("expiresAt", expiresAt.toIsoString())
    return json
}

fun JSONObject.toBankDetails(): BankDetails {
    return BankDetails(
        accountNumber = getString("accountNumber"),
        bankName = getString("name"),
        expiresAt = getString("expiresAt").toDate()
    )
}

fun Date.toIsoString(): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.format(this)
}

fun String.toDate(): Date {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return format.parse(this) ?: throw IllegalArgumentException("Invalid date format")
}


// Creating an example instance
val customerInfo = CustomerInfo(
    name = "John Doe",
    email = "john.doe@example.com",
    phoneNumber = "+1234567890"
)

val rate = Rate(
    from = "USD",
    to = "EUR",
    rate = 0.85
)

val authorization = Authorization(
    mode = "3DSecure",
    redirectUrl = "https://example.com/redirect"
)

val bankDetails = BankDetails(
    accountNumber = "123456789",
    bankName = "Bank of Example"
)

val examplePaymentResponseBody = PaymentResponseBody(
    id = "abc123",
    reference = "ref123",
    spotflowReference = "spot123",
    amount = 100.0,
    currency = "USD",
    channel = "card",
    status = "pending",
    customer = customerInfo,
    provider = "ExampleProvider",
    rate = rate,
    authorization = authorization,
    createdAt = Date(),
    providerMessage = "Authorization required",
    bankDetails = bankDetails
)