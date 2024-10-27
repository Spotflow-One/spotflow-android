package com.spotflow.models
import com.spotflow.R

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

    companion object {
        fun fromString(value: String): PaymentOptionsEnum? {
            return if(value.lowercase() == "card") {
                CARD
            } else if(value.lowercase() == "bank_transfer") {
                TRANSFER
            } else if(value.lowercase() == "ussd") {
                USSD
            } else
                null
        }
    }
    val title: String
        get() = when (this) {
            CARD -> "Pay with Card"
            TRANSFER -> "Pay with Transfer"
            USSD -> "Pay with USSD"
        }

}