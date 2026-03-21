package com.amur.pocky.data.local

import androidx.room.TypeConverter
import com.amur.pocky.data.model.BarcodeFormat

class Converters {
    @TypeConverter
    fun fromBarcodeFormat(format: BarcodeFormat): String = format.name

    @TypeConverter
    fun toBarcodeFormat(value: String): BarcodeFormat = BarcodeFormat.valueOf(value)
}
