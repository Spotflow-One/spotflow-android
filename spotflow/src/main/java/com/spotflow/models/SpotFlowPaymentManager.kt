package com.spotflow.models

import com.spotflow.R
import org.json.JSONObject

data class CustomerInfo(
    val email: String,
    val phoneNumber: String? = null,
    val name: String? = null,
    val id: String? = null


)

data class SpotFlowPaymentManager(
    val merchantId: String,
    val key: String,
    val encryptionKey: String,
    val customerEmail: String,
    val customerName: String? = null,
    val customerPhoneNumber: String? = null,
    val customerId: String? = null,
    val paymentDescription: String? = null,
    val appLogo: Int? = null, // Assuming Image resource ID
    val appName: String? = null,
    val planId: String
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
    key = "sk_test_d96889a82e9743bfb274c5f684ad5b69",
    encryptionKey = "g4ryTjP3VAGwl8Bk9r0foFtgoY64Ba4gZQ701OAAbB4=",
    customerEmail = "nkwachi@spotflow.one",
    paymentDescription = "League Pass",
    appLogo = R.drawable.nba_logo, // Assuming you have a drawable resource
    appName = "NBA",
    planId = "3c902af2-6bec-4cc1-a2a3-f74fc35e88d4"

)

