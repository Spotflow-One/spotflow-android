package com.spotflow.ui.utils
import com.spotflow.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.* // For Row, padding, etc.
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PciDssIcon() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // optional padding
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.shield_icon), // Replace with your actual resource
            contentDescription = "Shield Icon"
        )
        Text(
            text = "PCI DSS Certified",
            fontSize = 10.sp,
            color = Color(0xFF9E9BA1),
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}