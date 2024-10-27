package com.spotflow


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.ui.SpotFlowViewModel

class SpotFlowPaymentActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val merchantId = intent.getStringExtra("merchantId") ?: ""
        val key = intent.getStringExtra("key") ?: ""
        val encryptionKey = intent.getStringExtra("encryptionKey") ?: ""
        val customerEmail = intent.getStringExtra("customerEmail") ?: ""
        val customerName = intent.getStringExtra("customerName")
        val customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        val customerId = intent.getStringExtra("customerId")
        val paymentDescription = intent.getStringExtra("paymentDescription")
        val appLogo = intent.getIntExtra("appLogo", 0)
        val appName = intent.getStringExtra("appName")

        val planId = intent.getStringExtra("planId") ?: ""


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

        val paymentManager = SpotFlowPaymentManager(
            merchantId = merchantId,
            key = key,
            encryptionKey = encryptionKey,
            paymentDescription = paymentDescription,
            customerPhoneNumber = customerPhoneNumber,
            customerName = customerName,
            customerEmail = customerEmail,
            customerId = customerId,
            planId = planId,
            appLogo = appLogo,
            appName = appName
        );

        setContent {
            com.spotflow.ui.SpotFlowApp(
                viewModel = SpotFlowViewModel(paymentManager = paymentManager),
                closeSpotflow = {
                    finish()
                }
            )
        }
    }

    companion object {
        fun start(
            context: Context,
            paymentManager: SpotFlowPaymentManager,
            requestCode: Int,
        ) {
            val intent = Intent(context, SpotFlowPaymentActivity::class.java).apply {
                putExtra("merchantId", paymentManager.merchantId)
                putExtra("planId", paymentManager.planId)
                putExtra("key", paymentManager.key)
                putExtra("customerEmail", paymentManager.customerEmail)
                putExtra("customerName", paymentManager.customerName)
                putExtra("customerPhoneNumber", paymentManager.customerPhoneNumber)
                putExtra("customerId", paymentManager.customerId)
                putExtra("paymentDescription", paymentManager.paymentDescription)
                putExtra("appLogo", paymentManager.appLogo)
                putExtra("appName", paymentManager.appName)
                putExtra("encryptionKey", paymentManager.encryptionKey)

            }
            if (context is Activity) {
                context.startActivityForResult(intent, requestCode)
            } else {
                context.startActivity(intent)
            }
        }
    }
}
