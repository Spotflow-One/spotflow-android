package com.app.spotflowapp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.SpotFlowPaymentActivity
import com.spotflow.models.examplePaymentManager


@Composable
fun exampleSpotflow (){
    val context = LocalContext.current

    Column {
        Button(
            onClick = {
                SpotFlowPaymentActivity.start(
                    context = context,
                    paymentManager = examplePaymentManager,
                    requestCode = 200,
                )
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 121.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
            Text(
                text = "Start payment",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF3D3844)
            )
        }
    }
}