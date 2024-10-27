import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PciDssIcon
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.R



@Composable
fun ViewBanksUssdPage(
    paymentManager: com.spotflow.models.SpotFlowPaymentManager,
    rate: com.spotflow.models.Rate,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    amonut: Number
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp)
    ) {
        PaymentOptionTile(
            imageRes = R.drawable.ussd_icon, // Replace with appropriate drawable resource
            title = "Pay with USSD"
        )
        PaymentCard(
            paymentManager = paymentManager,
            rate = rate,
            amount = amonut
        )
        Spacer(modifier = Modifier.height(70.dp))
        Icon(
            painter = painterResource(id = R.drawable.ussd_icon), // Replace with appropriate drawable resource
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            tint = Color(0xFF01008E)
        )
        Spacer(modifier = Modifier.height(9.dp))
        Text(
            text = "Choose your bank to start the payment",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B)
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
        Spacer(modifier = Modifier.height(70.dp))
        BankSelectionTile(
            bankName = "First City Monument Bank",
            ussdCode = "*329#"
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CancelButton(
                onTap = onCancelButtonClicked
            )
            Spacer(modifier = Modifier.width(16.dp))
            ChangePaymentButton(
                onTap = onChangePaymentClicked
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        PciDssIcon()
        Spacer(modifier = Modifier.height(38.dp))
    }
}

@Composable
fun BankSelectionTile(bankName: String, ussdCode: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 16.dp)
            .border(
                width = 1.dp,
                color = Color(0xFFC0B5CF),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = bankName,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xFFCCCCE8),
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(horizontal = 7.dp, vertical = 2.dp)
        ) {
            Text(
                text = ussdCode,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            )
        }
    }
}
