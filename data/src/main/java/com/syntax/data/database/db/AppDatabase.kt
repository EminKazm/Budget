package com.syntax.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syntax.data.database.dao.TransactionDao
import com.syntax.domain.entities.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}
