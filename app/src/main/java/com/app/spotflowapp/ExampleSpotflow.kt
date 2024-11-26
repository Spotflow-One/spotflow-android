package com.app.spotflowapp

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.SpotFlowPaymentActivity
import com.spotflow.models.SpotFlowPaymentManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHomePage(title: String) {
    val context = LocalContext.current

    var email by remember { mutableStateOf(TextFieldValue("jon@snow.com")) }
    var planId by remember { mutableStateOf(TextFieldValue("")) }
    var merchantKey by remember { mutableStateOf(TextFieldValue("")) }
    var encryptionKey by remember { mutableStateOf(TextFieldValue("")) }
    var paymentDescription by remember { mutableStateOf(TextFieldValue("League Pass")) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        modifier = Modifier.background(Color.White),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Click the button below to start your payment",
                fontSize = 26.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                placeholder = { Text("email@sample.com") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = planId,
                onValueChange = { planId = it },
                label = { Text("Plan ID") },
                placeholder = { Text("plan id") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = merchantKey,
                onValueChange = { merchantKey = it },
                label = { Text("Merchant Key") },
                placeholder = { Text("merchant key") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = encryptionKey,
                onValueChange = { encryptionKey = it },
                label = { Text("Encryption Key") },
                placeholder = { Text("encryption key") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = paymentDescription,
                onValueChange = { paymentDescription = it },
                label = { Text("Payment Description") },
                placeholder = { Text("payment description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    if (email.text.isEmpty() || planId.text.isEmpty() ||
                        merchantKey.text.isEmpty() || encryptionKey.text.isEmpty()
                    ) {
                        Toast.makeText(
                            context,
                            "Please enter all required fields.",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    } else {
                        startPayment(
                            context,
                            email.text,
                            merchantKey.text,
                            encryptionKey.text,
                            paymentDescription.text,
                            planId.text
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Start Payment",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

private fun startPayment(
    context: Context,
    email: String,
    merchantKey: String,
    encryptionKey: String,
    paymentDescription: String,
    planId: String
) {
    val paymentManger = SpotFlowPaymentManager(
        customerEmail = email,
        key = merchantKey,
        encryptionKey = encryptionKey,
        planId = planId,
        paymentDescription = paymentDescription,
        appName = "NBA",
        customerName = "Test user",
        amount = 5
    )
    SpotFlowPaymentActivity.start(
        context = context,
        paymentManager = paymentManger,
        requestCode = 200,
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewMyHomePage() {
    MyHomePage(title = "Payment Page")
}
