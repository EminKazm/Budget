package com.syntax.data.di

import android.content.Context
import androidx.room.Room
import com.syntax.data.TransactionRepositoryImpl
import com.syntax.data.database.dao.AccountDao
import com.syntax.data.database.dao.TransactionDao
import com.syntax.data.database.db.AppDatabase
import com.syntax.data.database.db.AppDatabase.Companion.MIGRATION_1_2
import com.syntax.data.database.db.AppDatabase.Companion.MIGRATION_2_3
import com.syntax.domain.repository.TransactionRepository
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
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "budget_db"
        ).addMigrations(MIGRATION_1_2,MIGRATION_2_3 )
            .build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(db: AppDatabase): TransactionDao {
        return db.transactionDao()
    }

    @Provides
    @Singleton
    fun provideAccountDao(database: AppDatabase): AccountDao {
        return database.accountDao()
    }

}