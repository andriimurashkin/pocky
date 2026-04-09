package com.amur.pocky.data.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

data class Brand(
    val id: String,
    val displayName: String,
    val keywords: List<String>,
    @DrawableRes val logoRes: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val textColor: Color = Color.White,
)
