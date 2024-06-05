package com.syntax.data.database.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.syntax.data.database.dao.AccountDao
import com.syntax.data.database.dao.TransactionDao
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction

@Database(entities = [Transaction::class, Account::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun accountDao(): AccountDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `accounts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `currency` TEXT NOT NULL)"
                )
            }
        }
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add the new 'balance' column with a default value of 0.0
                database.execSQL("ALTER TABLE accounts ADD COLUMN balance REAL NOT NULL DEFAULT 0.0")
            }
        }
    }
}
