package com.app.spotflowapp.models

import com.app.spotflowapp.R
import org.json.JSONObject

data class CustomerInfo(
    val email: String,
    val phoneNumber: String? = null,
    val name: String? = null,
    val id: String? = null


)

data class SpotFlowPaymentManager(
    val merchantId: String,
    val paymentId: String,
    val fromCurrency: String,
    val toCurrency: String,
    val amount: Double,
    val key: String,
    val provider: String,
    val customerEmail: String,
    val customerName: String? = null,
    val customerPhoneNumber: String? = null,
    val customerId: String? = null,
    val paymentDescription: String? = null,
    val appLogo: Int? = null, // Assuming Image resource ID
    val appName: String? = null
) {
    val customer: CustomerInfo
        get() = CustomerInfo(
            email = customerEmail,
            phoneNumber = customerPhoneNumber,
            name = customerName,
            id = customerId
        )
}

val examplePaymentManager = SpotFlowPaymentManager(
    merchantId = "2f020a56-fbd3-40a3-ac90-d77d38399b6d",
    paymentId = "paymentId",
    fromCurrency = "USD",
    toCurrency = "NGN",
    amount = 10.0,
    key = "",
    provider = "flutterwave",
    customerEmail = "nkwachi@spotflow.one",
    paymentDescription = "League Pass",
    appLogo = R.drawable.nba_logo, // Assuming you have a drawable resource
    appName = "NBA",

)



fun CustomerInfo.toJson(): JSONObject {
    return JSONObject().apply {
        put("email", email)
        put("phoneNumber", phoneNumber)
        put("name", name)
        put("id", id)
    }
}

fun JSONObject.toCustomerInfo(): CustomerInfo {
    return CustomerInfo(
        email = getString("email"),
        phoneNumber = optString("phoneNumber", null.toString()),
        name = optString("name", null.toString()),
        id = optString("id", null.toString())
    )
}