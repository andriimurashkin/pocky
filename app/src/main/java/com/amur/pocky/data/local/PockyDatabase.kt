package com.amur.pocky.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amur.pocky.data.model.Card

@Database(entities = [Card::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PockyDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}
