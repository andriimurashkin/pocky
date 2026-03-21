package com.amur.pocky.data.repository

import com.amur.pocky.data.local.CardDao
import com.amur.pocky.data.model.Card
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardRepository @Inject constructor(
    private val cardDao: CardDao,
) {
    fun getAllCards(): Flow<List<Card>> = cardDao.getAllCards()

    fun getCardById(id: Long): Flow<Card?> = cardDao.getCardById(id)

    fun searchCards(query: String): Flow<List<Card>> = cardDao.searchCards(query)

    suspend fun saveCard(card: Card) {
        cardDao.upsertCard(card.copy(updatedAt = System.currentTimeMillis()))
    }

    suspend fun deleteCard(card: Card) {
        cardDao.deleteCard(card)
    }

    suspend fun toggleFavorite(card: Card) {
        cardDao.upsertCard(
            card.copy(
                isFavorite = !card.isFavorite,
                updatedAt = System.currentTimeMillis(),
            )
        )
    }
}
