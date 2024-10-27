package com.spotflow.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PaymentOptionTile(
    title: String,
    imageRes: Int,
    onTap: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTap?.invoke() }
            .padding(8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            draggedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .padding(8.dp)
                .background(Color.Transparent)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .size(24.dp)
            )
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF3D3844)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
