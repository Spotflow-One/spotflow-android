package com.spotflow.ui

import androidx.lifecycle.ViewModel
import com.spotflow.models.Bank
import com.spotflow.models.MerchantConfig
import com.spotflow.models.PaymentOptionsEnum
import com.spotflow.models.PaymentResponseBody
import com.spotflow.models.SpotFlowPaymentManager
import com.spotflow.models.SpotFlowUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SpotFlowViewModel(paymentManager: SpotFlowPaymentManager) : ViewModel() {


    private val _uiState = MutableStateFlow(
        SpotFlowUIState(
            paymentManager = paymentManager
        )
    )
    val uiState: StateFlow<SpotFlowUIState> = _uiState.asStateFlow()

    fun setMerchantConfig(merchantConfig: MerchantConfig) {
        _uiState.update { currentState ->
            currentState.copy(
                merchantConfig = merchantConfig,
            )
        }
    }

    fun setPaymentResponseBody(paymentResponseBody: PaymentResponseBody) {
        _uiState.update { currentState ->
            currentState.copy(paymentResponseBody = paymentResponseBody)
        }
    }

    fun setPaymentOptionEnum(paymentOptionsEnum: PaymentOptionsEnum) {
        _uiState.update { currentState ->
            currentState.copy(paymentOptionsEnum = paymentOptionsEnum)
        }
    }

    fun setBank(bank: Bank) {
        _uiState.update { currentState ->
            currentState.copy(
                bank = bank,
            )
        }
    }


    fun resetPayment() {
        _uiState.value =
            com.spotflow.models.SpotFlowUIState(
                paymentManager = _uiState.value.paymentManager,
                merchantConfig = _uiState.value.merchantConfig
            )
    }

}