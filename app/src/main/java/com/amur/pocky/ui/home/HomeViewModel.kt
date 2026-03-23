package com.amur.pocky.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amur.pocky.data.model.Card
import com.amur.pocky.data.repository.CardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CardRepository,
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    private val _barcodeCardId = MutableStateFlow<Long?>(null)
    val barcodeCardId: StateFlow<Long?> = _barcodeCardId

    fun showBarcode(cardId: Long) {
        _barcodeCardId.value = cardId
    }

    fun hideBarcode() {
        _barcodeCardId.value = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val cards: StateFlow<List<Card>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllCards()
            else repository.searchCards(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun toggleFavorite(card: Card) {
        viewModelScope.launch {
            repository.toggleFavorite(card)
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            repository.deleteCard(card)
        }
    }
}
