package com.amur.pocky.di

import android.content.Context
import androidx.room.Room
import com.amur.pocky.data.local.CardDao
import com.amur.pocky.data.local.PockyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PockyDatabase =
        Room.databaseBuilder(
            context,
            PockyDatabase::class.java,
            "pocky_database",
        ).build()

    @Provides
    fun provideCardDao(database: PockyDatabase): CardDao = database.cardDao()
}
