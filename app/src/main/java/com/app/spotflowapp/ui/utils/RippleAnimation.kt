package com.app.spotflowapp.ui.utils
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.spotflowapp.R
import kotlin.math.min


@Composable
fun RippleAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val rippleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier =
                Modifier.height(120.dp).width(120.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.height(50.dp).fillMaxWidth()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val maxRadius = min(canvasWidth, canvasHeight) / 2

            for (i in 0..2) {
                drawCircle(
                    color = Color(0xFF54C68E).copy(alpha = 1f - rippleProgress),
                    radius = maxRadius * rippleProgress * (i + 1),
                    center = Offset(canvasWidth / 2, canvasHeight / 2),
                )
            }
        }


        Box(
            modifier = Modifier
                .clip(CircleShape)
                .size(60.dp)
                .background(color = Color(0xFF54C68E))

        ) {
            Icon(
                Icons.Rounded.Check,
                contentDescription = "ji",
                tint = Color.White,
                modifier = Modifier.size(40.dp).align(alignment = Alignment.Center)

            )
        }
    }
}
