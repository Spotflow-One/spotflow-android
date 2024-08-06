package com.app.spotflowapp.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PinInputView(onComplete: (String) -> Unit) {
    var pin by remember { mutableStateOf(List(4) { "" }) }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        for (index in 0 until 4) {
            OutlinedTextField(
                value = pin[index],
                onValueChange = { newValue ->
                    if (newValue.length <= 1) {
                        pin = pin.toMutableList().apply { this[index] = newValue }
                        if (newValue.length == 1 && index < 3) {
                            focusManager.moveFocus(FocusDirection.Next)
                        } else if (newValue.length == 1) {
                            onComplete(pin.joinToString(""))
                            focusManager.clearFocus()
                        }
                    }
                },
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
                    .border(0.5.dp, Color(0xFFE6E6E7), RoundedCornerShape(4.dp)),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = VisualTransformation.None,
                isError = pin[index].length > 1
            )
        }
    }
}
