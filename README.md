## Android SDK

### Introduction

The Spotflow Android SDK empowers developers to seamlessly integrate payment functionalities into their Android applications. It provides UI components and methods that allow you accept payment in your Android app.

### Project Requirements

To integrate the Spotflow Android SDK into your project, ensure the following prerequisites are met:

- Android Studio 4.1 or later
- Android SDK API level 21 or higher while the minimum supported SDK version is 15
- Android Gradle Plugin 7.2 and above
- Gradle 7.1.3 and above
- AndroidX

<aside>
⚠️ **Beta Release**

The Android SDK is currently a beta release. If you encounter any issues, kindly reach out to our support team at support@spotflow.one. 

</aside>

### Installation

1. Add the Spotflow SDK dependency to your app-level `build.gradle` file: 
    
    ```groovy
    dependencies {
      implementation 'com.spotflow:spotflow-android:latest_version'
    }
    ```
    

2.  Add the Internet permission to your `AndroidManifest.xml` file:

```xml
<uses-permission android:name="android.permission.INTERNET" />

```

1. Sync your project with Gradle files to download the SDK into your project.

### Parameters Required by the Library

| Parameter | Type | Description |
| --- | --- | --- |
| `amount` | `Number` | The amount to be paid. |
| `planId` | `String` | The ID of the plan you want to pay for |
| `key` | `String` | The API key for authenticating the transaction. |
| `encryptionKey` | `String` | This key is used to encrypt the card. |
| `customerEmail` | `String` | The email address of the customer. |
| `customerName` | `String?` | The name of the customer (optional). |
| `customerPhoneNumber` | `String?` | The phone number of the customer (optional). |
| `customerId` | `String?` | The unique identifier for the customer (optional). |
| `paymentDescription` | `String?` | A description of the payment (optional). |
| `appLogo` | `Int?` | The resource ID for the app logo (optional). |
| `appName` | `String?` | The name of the app (optional). |

### Usage with Jetpack Compose

For users utilizing Jetpack Compose, you can directly use the composable function `PaymentUI`.

### Integration Example

```kotlin
// MainActivity.kt
package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.spotflow.compose.PaymentUI

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymentUI(
                customerEmail: "customer@example.com" ,
								customerName: "John Snow", //optional
							  customerPhoneNumber: "000-000-000", //optional
							  customerId: "unique_id" //optional
							  amount: "amount" 
							  planId: "plan_id",
							  key: "your_api_key",
							  encryptionKey: "encryption_key",
							  paymentDescription: "Product purchase",
                onSuccess = { transactionId, paymentData ->
                    // Handle successful payment
                },
                onFailure = { errorCode, errorMessage ->
                    // Handle payment failure
                }
            )
        }
    }
}

```

### Usage by Launching an Activity

For users who do not use Jetpack Compose, you can integrate the payment functionality by launching a provided activity.

### Integration Example

```kotlin
// MainActivity.kt
package com.example.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spotflow.compose.PaymentActivity

class MainActivity : AppCompatActivity() {

    private val PAYMENT_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize payment
        SpotFlowPaymentActivity.start(
                customerEmail: "customer@example.com" ,
								customerName: "John Snow", //optional
							  customerPhoneNumber: "000-000-000", //optional
							  customerId: "unique_id" //optional
							  amount: "amount" 
							  planId: "plan_id",
							  key: "your_api_key",
							  encryptionKey: "encryption_key",
							  paymentDescription: "Product purchase",
            requestCode = PAYMENT_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val transactionId = data?.getStringExtra("transactionId")
                val paymentData = data?.getSerializableExtra("paymentData") as? Map<String, Any>
                // Handle successful payment
            } else if (resultCode == Activity.RESULT_CANCELED) {
                val errorCode = data?.getStringExtra("errorCode")
                val errorMessage = data?.getStringExtra("errorMessage")
                // Handle payment failure
            }
        }
    }
}

```

### Help

> Feel free to create issues and pull requests.
>
