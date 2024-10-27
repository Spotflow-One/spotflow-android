package com.spotflow.ui.utils


import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardTextField(
    value: String,
    label: String,
    hintText: String,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    Column(
        modifier = modifier

    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.5.dp),
        ) {
            Text(
                text = label,
                color = Color(0xFF9E9BA1),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                visualTransformation = visualTransformation,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6D6A73)
                ),
                decorationBox = {innerTextField ->
                    TextFieldDefaults.DecorationBox(
                        value = value,
                        visualTransformation = visualTransformation,
                        innerTextField = innerTextField,
                        singleLine = true,
                        enabled = true,
                        interactionSource = interactionSource,
                        contentPadding = PaddingValues(0.dp),

                        placeholder = {
                            Text(
                                text = hintText,
                                color = Color(0xFFB6B4B9),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),

                    )
                }



            )
        }
    }
}
