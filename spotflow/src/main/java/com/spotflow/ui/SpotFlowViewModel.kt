package com.spotflow.ui

import androidx.lifecycle.ViewModel
import com.spotflow.models.SpotFlowPaymentManager

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class SpotFlowViewModel(paymentManager: SpotFlowPaymentManager) : ViewModel() {


    private val _uiState = MutableStateFlow(
        com.spotflow.models.SpotFlowUIState(
            paymentManager = paymentManager
        )
    )
    val uiState: StateFlow<com.spotflow.models.SpotFlowUIState> = _uiState.asStateFlow()

    fun setMerchantConfig(merchantConfig: com.spotflow.models.MerchantConfig) {
        _uiState.update { currentState ->
            currentState.copy(
                merchantConfig = merchantConfig,
            )
        }
    }

    fun setPaymentResponseBody(paymentResponseBody: com.spotflow.models.PaymentResponseBody) {
        _uiState.update { currentState ->
            currentState.copy(paymentResponseBody = paymentResponseBody)
        }
    }

    fun setPaymentOptionEnum(paymentOptionsEnum: com.spotflow.models.PaymentOptionsEnum) {
        _uiState.update { currentState ->
            currentState.copy(paymentOptionsEnum = paymentOptionsEnum)
        }
    }


    fun resetPayment() {
        _uiState.value =
            com.spotflow.models.SpotFlowUIState(paymentManager = _uiState.value.paymentManager, merchantConfig = _uiState.value.merchantConfig)
    }

}