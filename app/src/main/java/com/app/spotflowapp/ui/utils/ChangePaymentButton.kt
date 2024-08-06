package com.app.spotflowapp.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.app.spotflowapp.ui.theme.SpotFlowAppTheme

@Composable
fun ChangePaymentButton(
    onTap: (() -> Unit)? = null
) {
    Button(
        onClick = { onTap?.invoke()},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color(0xFFE6E6E7))
    ) {
        Text(
            text = "x Change Payment Method",
            color = Color(0xFF55515B),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ChangePaymentButtonPreview() {
    SpotFlowAppTheme {

        ChangePaymentButton()
    }
}
