package com.amur.pocky.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.amur.pocky.data.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY isFavorite DESC, updatedAt DESC")
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getCardById(id: Long): Flow<Card?>

    @Query(
        "SELECT * FROM cards WHERE name LIKE '%' || :query || '%' " +
            "OR cardNumber LIKE '%' || :query || '%' " +
            "ORDER BY isFavorite DESC, updatedAt DESC"
    )
    fun searchCards(query: String): Flow<List<Card>>

    @Upsert
    suspend fun upsertCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)
}
