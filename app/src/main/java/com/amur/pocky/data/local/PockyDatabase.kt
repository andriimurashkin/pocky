package com.amur.pocky.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amur.pocky.data.model.Card

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE cards ADD COLUMN brandId TEXT DEFAULT NULL")
    }
}

@Database(entities = [Card::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class PockyDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}
