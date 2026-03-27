package com.amur.pocky.ui.addcard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amur.pocky.data.model.BarcodeFormat
import com.amur.pocky.data.model.Card
import com.amur.pocky.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddCardUiState(
    val name: String = "",
    val cardNumber: String = "",
    val barcodeFormat: BarcodeFormat = BarcodeFormat.CODE_128,
    val note: String = "",
    val color: Int? = null,
    val isEditing: Boolean = false,
    val isSaved: Boolean = false,
    val isScanned: Boolean = false,
    val cardNumberError: String? = null,
) {
    val isValid: Boolean
        get() = name.isNotBlank() && cardNumber.isNotBlank() && cardNumberError == null
}

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val repository: CardRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val editCardId: Long? = savedStateHandle.get<Long>("cardId")

    private val _uiState = MutableStateFlow(AddCardUiState())
    val uiState: StateFlow<AddCardUiState> = _uiState.asStateFlow()

    init {
        if (editCardId != null) {
            viewModelScope.launch {
                repository.getCardById(editCardId).collect { card ->
                    if (card != null) {
                        _uiState.update {
                            it.copy(
                                name = card.name,
                                cardNumber = card.cardNumber,
                                barcodeFormat = card.barcodeFormat,
                                note = card.note,
                                color = card.color,
                                isEditing = true,
                            )
                        }
                    }
                }
            }
        }
    }

    fun initFromScan(data: String?, format: String?) {
        if (data == null) return
        val detectedFormat = format?.let { f ->
            try { BarcodeFormat.valueOf(f) } catch (_: Exception) { null }
        } ?: BarcodeFormat.EAN_13
        _uiState.update {
            it.copy(
                cardNumber = data,
                barcodeFormat = detectedFormat,
                isScanned = true,
                cardNumberError = null,
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onCardNumberChange(number: String) {
        val digitsOnly = number.filter { it.isDigit() }
        val detected = detectBarcodeFormat(digitsOnly)
        _uiState.update {
            it.copy(
                cardNumber = digitsOnly,
                barcodeFormat = detected.first,
                cardNumberError = detected.second,
            )
        }
    }

    private fun detectBarcodeFormat(number: String): Pair<BarcodeFormat, String?> {
        if (number.isEmpty()) return BarcodeFormat.CODE_128 to null
        return when (number.length) {
            8 -> BarcodeFormat.EAN_8 to validateEanCheckDigit(number)
            12 -> BarcodeFormat.UPC_A to validateEanCheckDigit(number)
            13 -> BarcodeFormat.EAN_13 to validateEanCheckDigit(number)
            else -> BarcodeFormat.CODE_128 to null
        }
    }

    private fun validateEanCheckDigit(number: String): String? {
        if (!number.all { it.isDigit() }) return "Must contain only digits"
        val digits = number.map { it.digitToInt() }
        val sum = digits.dropLast(1).mapIndexed { i, d ->
            if (i % 2 == 0) d else d * 3
        }.sum()
        val checkDigit = (10 - sum % 10) % 10
        if (checkDigit != digits.last()) return "Invalid check digit"
        return null
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onColorChange(color: Int?) {
        _uiState.update { it.copy(color = color) }
    }

    fun saveCard() {
        val state = _uiState.value
        if (!state.isValid) return

        viewModelScope.launch {
            val card = Card(
                id = editCardId ?: 0,
                name = state.name.trim(),
                cardNumber = state.cardNumber.trim(),
                barcodeFormat = state.barcodeFormat,
                note = state.note.trim(),
                color = state.color,
            )
            repository.saveCard(card)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}
