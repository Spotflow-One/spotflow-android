package com.spotflow.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.ui.tooling.preview.Preview
import com.spotflow.ui.theme.SpotFlowAppTheme
import com.spotflow.ui.views.EnterBillingAddressPage


@Composable
fun CardDropdown(
    modifier: Modifier = Modifier,
    labelText: String,
    text: String? = null,
    hintText: String,
    enabled: Boolean
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = labelText,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row {
            Text(
                text = text ?: hintText,
                color = if (text != null) Color(0xFF55515B) else Color.Gray,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            IconRight(color = if(enabled) Color.Black  else Color.Gray)
        }


    }




}

@Composable
fun IconRight(color: Color) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowDown,
        contentDescription = null,
        tint = color
    )
}


@Preview(showBackground = true)
@Composable
fun CardDropdownPreview() {
    SpotFlowAppTheme {

        CardDropdown(
            labelText = "Country",
            hintText = "Enter country",
            enabled = true
        )
    }
}
