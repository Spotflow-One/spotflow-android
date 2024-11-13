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


