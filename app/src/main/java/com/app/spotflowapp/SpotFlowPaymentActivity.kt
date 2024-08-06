package com.app.spotflowapp

// PaymentActivity.kt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.spotflowapp.ui.SpotFlowApp

class SpotFlowPaymentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val merchantId = intent.getStringExtra("merchantId") ?: ""
        val paymentId = intent.getStringExtra("paymentId") ?: ""
        val fromCurrency = intent.getStringExtra("fromCurrency") ?: ""
        val toCurrency = intent.getStringExtra("toCurrency") ?: ""
        val amount = intent.getDoubleExtra("amount", 0.0)
        val key = intent.getStringExtra("key") ?: ""
        val provider = intent.getStringExtra("provider") ?: ""
        val customerEmail = intent.getStringExtra("customerEmail") ?: ""
        val customerName = intent.getStringExtra("customerName")
        val customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        val customerId = intent.getStringExtra("customerId")
        val paymentDescription = intent.getStringExtra("paymentDescription")
        val appLogo = intent.getIntExtra("appLogo", 0)
        val appName = intent.getStringExtra("appName")

        val onSuccess: (String, Map<String, Any>) -> Unit = { transactionId, paymentData ->
            setResult(RESULT_OK, Intent().apply {
                putExtra("transactionId", transactionId)
                putExtra("paymentData", HashMap(paymentData))
            })
            finish()
        }
        val onFailure: (String, String) -> Unit = { errorCode, errorMessage ->
            setResult(RESULT_CANCELED, Intent().apply {
                putExtra("errorCode", errorCode)
                putExtra("errorMessage", errorMessage)
            })
            finish()
        }

        setContent {
            SpotFlowApp(
            )
        }
    }

    companion object {
        fun start(
            context: Context,
            merchantId: String,
            paymentId: String,
            fromCurrency: String,
            toCurrency: String,
            amount: Double,
            key: String,
            provider: String,
            customerEmail: String,
            customerName: String? = null,
            customerPhoneNumber: String? = null,
            customerId: String? = null,
            paymentDescription: String? = null,
            appLogo: Int? = null,
            appName: String? = null,
            requestCode: Int
        ) {
            val intent = Intent(context, SpotFlowPaymentActivity::class.java).apply {
                putExtra("merchantId", merchantId)
                putExtra("paymentId", paymentId)
                putExtra("fromCurrency", fromCurrency)
                putExtra("toCurrency", toCurrency)
                putExtra("amount", amount)
                putExtra("key", key)
                putExtra("provider", provider)
                putExtra("customerEmail", customerEmail)
                putExtra("customerName", customerName)
                putExtra("customerPhoneNumber", customerPhoneNumber)
                putExtra("customerId", customerId)
                putExtra("paymentDescription", paymentDescription)
                putExtra("appLogo", appLogo)
                putExtra("appName", appName)
            }
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
    }
}
