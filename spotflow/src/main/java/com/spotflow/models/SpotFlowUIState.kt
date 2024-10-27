package com.spotflow.models

data class SpotFlowUIState (
    val paymentManager: SpotFlowPaymentManager,
    val paymentResponseBody: PaymentResponseBody? = null,
    val merchantConfig: MerchantConfig? = null,
    val paymentOptionsEnum: PaymentOptionsEnum? = null
    ) {
    val amount: Number get() = merchantConfig!!.plan.amount
    val rate: Rate
        get() =  merchantConfig!!.rate
}

