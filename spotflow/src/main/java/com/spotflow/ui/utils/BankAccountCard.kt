package com.spotflow.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import java.util.Locale
import com.spotflow.R


@Composable
fun BankAccountCard(bankDetails: com.spotflow.models.BankDetails, formattedAmount: String, amount: Double) {
    val clipboardManager: androidx.compose.ui.platform.ClipboardManager = LocalClipboardManager.current

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(Color(0xFFF4F4FF), shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 11.dp, vertical = 23.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "BANK NAME",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6D6A73),
                    modifier = Modifier.padding(bottom = 0.2.dp)
                )
                Text(
                    text = bankDetails.bankName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF3D3844)
                )
            }
//            Button(
//                onClick = { /*TODO: Change Bank*/ },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
//                contentPadding = PaddingValues(0.dp)
//            ) {
//                Text(
//                    text = "CHANGE BANK",
//                    fontSize = 10.sp,
//                    fontWeight = FontWeight.Normal,
//                    color = Color(0xFF6D6A73)
//                )
//            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ACCOUNT NUMBER",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6D6A73),
                    modifier = Modifier.padding(bottom = 0.2.dp)
                )
                Text(
                    text = bankDetails.accountNumber,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF3D3844)
                )
            }
            Button(
                onClick = {
                    clipboardManager.setText(annotatedString = AnnotatedString(text = bankDetails.accountNumber))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.copy_icon),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "AMOUNT",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF6D6A73),
                    modifier = Modifier.padding(bottom = 0.2.dp)
                )
                Text(
                    text = formattedAmount,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF3D3844)
                )
            }
            Button(
                onClick = {
                    val amountTo2dp = String.format(Locale.getDefault(),"%.2f", amount) ;
                    clipboardManager.setText(annotatedString = AnnotatedString(text = amountTo2dp))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                contentPadding = PaddingValues(0.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.copy_icon),
                    contentDescription = null,
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}


