package com.amur.pocky.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val cardNumber: String,
    val barcodeFormat: BarcodeFormat,
    val note: String = "",
    val color: Int? = null,
    val brandId: String? = null,
    val isFavorite: Boolean = false,
    val category: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)
