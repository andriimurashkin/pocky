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
    val barcodeFormat: BarcodeFormat = BarcodeFormat.EAN_13,
    val note: String = "",
    val color: Int? = null,
    val isEditing: Boolean = false,
    val isSaved: Boolean = false,
)

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
        _uiState.update {
            it.copy(
                cardNumber = data,
                barcodeFormat = format?.let { f ->
                    try { BarcodeFormat.valueOf(f) } catch (_: Exception) { null }
                } ?: BarcodeFormat.EAN_13,
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onCardNumberChange(number: String) {
        _uiState.update { it.copy(cardNumber = number) }
    }

    fun onFormatChange(format: BarcodeFormat) {
        _uiState.update { it.copy(barcodeFormat = format) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onColorChange(color: Int?) {
        _uiState.update { it.copy(color = color) }
    }

    fun saveCard() {
        val state = _uiState.value
        if (state.name.isBlank() || state.cardNumber.isBlank()) return

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
