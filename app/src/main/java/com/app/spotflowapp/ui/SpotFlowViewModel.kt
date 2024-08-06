package com.app.spotflowapp.ui

import androidx.lifecycle.ViewModel
import com.app.spotflowapp.models.PaymentOptionsEnum
import com.app.spotflowapp.models.PaymentResponseBody
import com.app.spotflowapp.models.Rate
import com.app.spotflowapp.models.SpotFlowPaymentManager
import com.app.spotflowapp.models.SpotFlowUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * [SpotFlowViewModel] holds information about a cupcake order in terms of quantity, flavor, and
 * pickup date. It also knows how to calculate the total price based on these order details.
 */
class SpotFlowViewModel(paymentManager: SpotFlowPaymentManager) : ViewModel() {


    /**
     * Cupcake state for this order
     */
    private val _uiState = MutableStateFlow(SpotFlowUIState(paymentManager = paymentManager))
    val uiState: StateFlow<SpotFlowUIState> = _uiState.asStateFlow()

    fun setRate(rate: Rate) {
        _uiState.update { currentState ->
            currentState.copy(
                rate = rate,
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


    fun resetPayment() {
        _uiState.value = SpotFlowUIState(paymentManager = _uiState.value.paymentManager)
    }

}