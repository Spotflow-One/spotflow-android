package com.app.spotflowapp.models

data class SpotFlowUIState (
    val paymentManager: SpotFlowPaymentManager,
    val paymentResponseBody: PaymentResponseBody? = null,
    val rate: Rate? = null,
    val paymentOptionsEnum: PaymentOptionsEnum? = null
    )

