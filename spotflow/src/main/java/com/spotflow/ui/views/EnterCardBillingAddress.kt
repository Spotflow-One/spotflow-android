package com.spotflow.ui.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.spotflow.R
import com.spotflow.models.Avs
import com.spotflow.models.AvsAuthorization
import com.spotflow.models.AvsPaymentRequestBody
import com.spotflow.models.BaseModel
import com.spotflow.models.City
import com.spotflow.models.Country
import com.spotflow.models.CountryState
import com.spotflow.models.PaymentResponseBody
import com.spotflow.models.Rate
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.ui.utils.BottomSheetWithSearch
import com.spotflow.ui.utils.CancelButton
import com.spotflow.ui.utils.CardDropdown
import com.spotflow.ui.utils.CardTextField
import com.spotflow.ui.utils.ChangePaymentButton
import com.spotflow.ui.utils.PaymentCard
import com.spotflow.ui.utils.PaymentOptionTile
import com.spotflow.ui.utils.PciDssIcon
import retrofit2.Call
import retrofit2.Callback

@Composable
fun EnterBillingAddressPage(
    paymentResponseBody: PaymentResponseBody,
    paymentManager: SpotFlowPaymentManager,
    onSuccessfulPayment: (paymentResponse: PaymentResponseBody) -> Unit,
    rate: Rate,

    ) {
    BillingAddressContent(
        paymentResponseBody = paymentResponseBody,
        paymentManager = paymentManager,
        rate = rate,
        onSuccessfulPayment = onSuccessfulPayment,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillingAddressContent(
    paymentResponseBody: PaymentResponseBody,
    paymentManager: SpotFlowPaymentManager,
    onSuccessfulPayment: (paymentResponse: PaymentResponseBody) -> Unit,
    rate: Rate,

    ) {
    var address by remember { mutableStateOf("") }
    var zipCode by remember { mutableStateOf("") }
    var country: Country? by remember { mutableStateOf(null) }
    var state: CountryState? by remember { mutableStateOf(null) }
    var city: City? by remember { mutableStateOf(null) }
    var countries by remember { mutableStateOf(emptyList<Country>()) }
    val isCreatingPayment: MutableState<Boolean> = remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val sheetState = rememberModalBottomSheetState()
    var showCountryBottomSheet by remember { mutableStateOf(false) }
    var showStateBottomSheet by remember { mutableStateOf(false) }
    var showCityBottomSheet by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        countries = loadCountriesJson(context)
    }

    if (showCountryBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showCountryBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            BottomSheetWithSearch(
                items = countries as List<BaseModel>,
                onSelect = {
                    country = it as Country
                    state = null
                    city = null
                    showCountryBottomSheet = false
                }
            )
        }
    } else if (showStateBottomSheet && country?.states != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showStateBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            BottomSheetWithSearch(
                items = country?.states as List<BaseModel>,
                onSelect = {
                    state = it as CountryState
                    city = null
                    showStateBottomSheet = false
                }
            )
        }
    } else if (showCityBottomSheet && state?.cities != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showCityBottomSheet = false
            },
            sheetState = sheetState
        ) {
            // Sheet content
            BottomSheetWithSearch(
                items = state?.cities as List<BaseModel>,
                onSelect = {
                    city = it as City
                    showCityBottomSheet = false
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        PaymentOptionTile(
            title = "Pay with Card",
            imageRes = R.drawable.pay_with_card_icon
        )

        PaymentCard(
            paymentManager = paymentManager,
            rate = rate,
            amount = paymentResponseBody.amount,
        )

        if (isCreatingPayment.value) {
            Box(
                modifier = Modifier.fillMaxSize().padding(64.dp),
                contentAlignment = Alignment.Center

            ) {
                CircularProgressIndicator(
                    modifier = Modifier.height(50.dp)

                )
            }
        } else {
            Spacer(modifier = Modifier.height(34.dp))
            Text(
                "Fill in Your Billing Address", fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF55515B),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            CardTextField(
                value = address,
                label = "Address",
                hintText = "Osapa London",
                visualTransformation = CapitalizeFirstLetterTransformation(),

                onValueChange = {
                    address = it
                    validateAddress(
                        address, zipCode, country, state, city,
                        onValidChange = { isButtonEnabled = it }
                    )
                },
                modifier = Modifier.padding(vertical = 4.5.dp)
                    .padding(horizontal = 16.dp)
                    .border(
                        BorderStroke(1.dp, Color(0xFFE6E6E7)),
                        shape = RoundedCornerShape(5.dp)
                    )
                    .fillMaxWidth()


            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                val countryEnabled = countries.isNotEmpty()
                CountryDropdown(
                    selectedCountry = country,
                    enabled = countryEnabled,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp)
                        .clickable(enabled = countryEnabled) {
                            showCountryBottomSheet = true;
                        }
                        .border(
                            BorderStroke(0.5.dp, Color(0xFFE6E6E7)),
                            RoundedCornerShape(6.dp)
                        ).padding(horizontal = 16.dp, vertical = 4.5.dp)
                        .weight(1f)// Adding the weight modifier

                )

                val stateEnabled = country?.states?.isNotEmpty() ?: false
                StateDropdown(
                    selectedState = state,
                    enabled = stateEnabled,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp, end = 16.dp)
                        .clickable(enabled = stateEnabled) {
                            showStateBottomSheet = true
                        }
                        .border(
                            BorderStroke(0.5.dp, Color(0xFFE6E6E7)),
                            RoundedCornerShape(6.dp)
                        ).padding(horizontal = 16.dp, vertical = 4.5.dp)
                        .weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val cityEnabled = state?.cities?.isNotEmpty() ?: false
            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                CityDropdown(
                    selectedCity = city,
                    enabled = cityEnabled,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 4.dp)
                        .clickable(enabled = cityEnabled) {
                            showCityBottomSheet = true
                        }
                        .border(
                            BorderStroke(0.5.dp, Color(0xFFE6E6E7)),
                            RoundedCornerShape(6.dp)
                        ).padding(horizontal = 16.dp, vertical = 4.5.dp)
                        .weight(1f)
                )


                CardTextField(
                    value = zipCode,
                    label = "Zipcode",
                    hintText = "000 000",
                    visualTransformation = CapitalizeFirstLetterTransformation(),
                    onValueChange = {
                        zipCode = it
                        validateAddress(
                            address, zipCode, country, state, city,
                            onValidChange = { isButtonEnabled = it }
                        )
                    },
                    modifier = Modifier.padding(vertical = 4.5.dp)
                        .padding(horizontal = 16.dp)
                        .border(
                            BorderStroke(1.dp, Color(0xFFE6E6E7)),
                            shape = RoundedCornerShape(5.dp)
                        ).weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val avs = AvsPaymentRequestBody(
                        reference = paymentResponseBody.reference,
                        authorization = AvsAuthorization(
                            avs = Avs(
                                address = address,
                                city = city?.name ?: "",
                                state = state?.name ?: "",
                                country = country?.name ?: "",
                                zipCode = zipCode,
                            )
                        ),
                        merchantId = paymentResponseBody.id
                    )

                    submitAddressDetails(
                        paymentManager = paymentManager,
                        avsPaymentRequestBody = avs,
                        loading = isCreatingPayment,
                        onSuccessfulPayment = onSuccessfulPayment
                    )

                },
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                enabled = isButtonEnabled,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF01008E),
                    disabledContainerColor = Color(0xFFF4F4FF)
                )
            ) {
                Text(
                    "Submit",
                    color = if (isButtonEnabled) Color.White else Color(0xFFAAAAD9),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

            }

            Spacer(modifier = Modifier.height(60.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                ChangePaymentButton()
                Spacer(modifier = Modifier.width(16.dp))
                CancelButton()
            }
            Spacer(modifier = Modifier.height(46.dp))
            PciDssIcon()
        }

    }
}

@Composable
fun CountryDropdown(
    selectedCountry: Country?,
    modifier: Modifier = Modifier,
    enabled: Boolean
) {
    CardDropdown(
        labelText = "Country",
        hintText = "Enter country",
        text = selectedCountry?.name,
        modifier = modifier,
        enabled = enabled,
    )

}

@Composable
fun StateDropdown(
    selectedState: CountryState?,
    modifier: Modifier,
    enabled: Boolean

) {

    CardDropdown(
        labelText = "State",
        hintText = "Enter state",
        text = selectedState?.name,
        modifier = modifier,
        enabled = enabled
    )
}

@Composable
fun CityDropdown(
    selectedCity: City?,
    modifier: Modifier,
    enabled: Boolean

) {
    CardDropdown(
        labelText = "City",
        hintText = "Enter city",
        text = selectedCity?.name,
        modifier = modifier,
        enabled = enabled
    )
}

// Function to validate the address fields
fun validateAddress(
    address: String,
    zipCode: String,
    country: Country?,
    state: CountryState?,
    city: City?,
    onValidChange: (Boolean) -> Unit
) {
    onValidChange(
        address.isNotEmpty() &&
                zipCode.isNotEmpty() &&
                country != null &&
                state != null &&
                city != null
    )
}


private fun submitAddressDetails(
    paymentManager: SpotFlowPaymentManager,
    avsPaymentRequestBody: AvsPaymentRequestBody,
    loading: MutableState<Boolean>,
    onSuccessfulPayment: (paymentResponse: PaymentResponseBody) -> Unit

) {
    val authToken = paymentManager.key
    val retrofit = com.spotflow.core.ApiClient.getRetrofit(authToken)
    val apiService = retrofit.create(com.spotflow.core.PaymentApi::class.java)

    loading.value = true

    val call =
        apiService.authorizeAvsPayment(avsPaymentRequestBody)
    // on below line we are executing our method.
    call!!.enqueue(object : Callback<PaymentResponseBody?> {
        override fun onResponse(
            call: Call<PaymentResponseBody?>,
            response: retrofit2.Response<PaymentResponseBody?>
        ) {
            // this method is called when we get response from our api.

            if (response.code() == 200) {
                // we are getting a response from our body and
                // passing it to our model class.
                val model: PaymentResponseBody? = response.body()
                if (model != null) {
                    onSuccessfulPayment.invoke(model)
                }
            }
            loading.value = false
        }

        override fun onFailure(call: Call<PaymentResponseBody?>, t: Throwable) {
            loading.value = false

        }
    })
}


fun loadCountriesJson(context: android.content.Context): List<Country> {
    val jsonString =
        context.assets.open("countries+states+cities.json").bufferedReader().use { it.readText() }
    val gson = Gson()
    val c: List<Country> = gson.fromJson(jsonString, object : TypeToken<List<Country>>() {}.type)

    return c
}


class CapitalizeFirstLetterTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = text.text.capitalizeFirstLetter()
        return TransformedText(AnnotatedString(transformedText), OffsetMapping.Identity)
    }
}

// Extension function to capitalize the first letter of each word
fun String.capitalizeFirstLetter(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}


