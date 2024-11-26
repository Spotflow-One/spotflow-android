package com.spotflow.models


data class CustomerInfo(
    val email: String,
    val phoneNumber: String? = null,
    val name: String? = null,
    val id: String? = null
)

data class SpotFlowPaymentManager(
    val key: String,
    val encryptionKey: String,
    val customerEmail: String,
    val customerName: String? = null,
    val customerPhoneNumber: String? = null,
    val customerId: String? = null,
    val paymentDescription: String? = null,
    val appLogo: Int? = null, // Assuming Image resource ID
    val appName: String? = null,
    val planId: String? = null,
    val amount: Number
) {
    val customer: CustomerInfo
        get() = CustomerInfo(
            email = customerEmail,
            phoneNumber = customerPhoneNumber,
            name = customerName,
            id = customerId
        )
}


