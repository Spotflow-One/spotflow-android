package com.app.spotflowapp.models
import com.app.spotflowapp.R

enum class PaymentOptionsEnum {
    CARD,
    TRANSFER,
    USSD;

    val icon: Int
        get() = when (this) {
            CARD -> R.drawable.pay_with_card_icon
            TRANSFER -> R.drawable.pay_with_transfer_icon
            USSD -> R.drawable.ussd_icon
        }

    val title: String
        get() = when (this) {
            CARD -> "Pay with Card"
            TRANSFER -> "Pay with Transfer"
            USSD -> "Pay with USSD"
        }
}