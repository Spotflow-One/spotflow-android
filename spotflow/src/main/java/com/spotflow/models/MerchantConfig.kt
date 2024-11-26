package com.spotflow.models
 class MerchantConfig(
     val merchantLogo: String,
     val merchantName: String,
     val paymentMethods: List<String>,
     val plan: SpotFlowPlan? = null,
     val rate: Rate,
)

 class SpotFlowPlan(
    val id: String,
    val title: String,
    val frequency: String,
    val amount: Number,
    val currency: String,
    val status: String,
)