package com.amur.pocky.ui.carddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amur.pocky.data.model.Card
import com.amur.pocky.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val repository: CardRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val cardId: Long = savedStateHandle["cardId"]!!

    val card: StateFlow<Card?> = repository.getCardById(cardId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted.asStateFlow()

    fun toggleFavorite() {
        val currentCard = card.value ?: return
        viewModelScope.launch {
            repository.toggleFavorite(currentCard)
        }
    }

    fun deleteCard() {
        val currentCard = card.value ?: return
        viewModelScope.launch {
            repository.deleteCard(currentCard)
            _isDeleted.value = true
        }
    }
}
