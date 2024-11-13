import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.spotflow.models.Bank
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.ui.utils.BottomSheetWithSearch
import retrofit2.Call
import retrofit2.Callback


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewBanksUssdPage(
    paymentManager: SpotFlowPaymentManager,
    rate: com.spotflow.models.Rate,
    onCancelButtonClicked: () -> Unit,
    onChangePaymentClicked: () -> Unit,
    amount: Number,
    createPayment: (Bank) -> Unit,
) {

    val loading: MutableState<Boolean> = remember { mutableStateOf(false) }
    val banks: MutableState<List<Bank>> = remember { mutableStateOf(emptyList()) }
    val sheetState = rememberModalBottomSheetState()

    val selectedBank: MutableState<Bank?> = remember { mutableStateOf(null) }

    var showBanksBottomSheet by remember { mutableStateOf(false) }

    if (showBanksBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBanksBottomSheet = false
            }, sheetState = sheetState
        ) {
            // Sheet content
            BottomSheetWithSearch(items = banks.value, onSelect = {
                selectedBank.value = it as Bank
                showBanksBottomSheet = false
                createPayment.invoke(selectedBank.value!!);
            })
        }
    }


    LaunchedEffect(Unit) {
        getBanks(
            paymentManager = paymentManager,
            bankState = banks,
            loading = loading,
        )
    }

    if (loading.value) {
        Box(
            modifier = Modifier.fillMaxSize().padding(64.dp), contentAlignment = Alignment.Center

        ) {
            CircularProgressIndicator(
                modifier = Modifier.height(50.dp)
            )
        }

    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
        ) {
            PaymentOptionTile(
                imageRes = R.drawable.ussd_icon, // Replace with appropriate drawable resource
                title = "Pay with USSD"
            )
            PaymentCard(
                paymentManager = paymentManager, rate = rate, amount = amount
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
                    fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF55515B)
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(70.dp))
            BankSelectionTile(bankName = selectedBank.value?.name ?: "", onClick = {
                showBanksBottomSheet = true
            })
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
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


}

@Composable
fun BankSelectionTile(
    bankName: String, onClick: () -> Unit // Lambda to handle the click event
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp, horizontal = 16.dp).border(
        width = 1.dp, color = Color(0xFFC0B5CF), shape = RoundedCornerShape(8.dp)
    ).clickable { onClick() } // Add clickable modifier here
        .padding(horizontal = 16.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = bankName, style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}


private fun getBanks(
    paymentManager: SpotFlowPaymentManager,
    bankState: MutableState<List<Bank>>,
    loading: MutableState<Boolean>,
) {
    loading.value = true
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)
    val call = apiService.getBanks(ussd = true)

    Log.d("Banks", "getting banks")
    // on below line we are executing our method.
    call.enqueue(object : Callback<List<Bank>?> {
        override fun onResponse(
            call: Call<List<Bank>?>, response: retrofit2.Response<List<Bank>?>
        ) {
            Log.d("Banks", "response code is ${response.code()}")
            if (response.code() == 200 && response.body() != null) {
                bankState.value = response.body()!!
                Log.d("Banks", "list length is ${bankState.value.size}")
            }
            loading.value = false
        }

        override fun onFailure(call: Call<List<Bank>?>, t: Throwable) {
            loading.value = false
            Log.d("Banks", "getting banks ${t.message}")

        }
    })


}

