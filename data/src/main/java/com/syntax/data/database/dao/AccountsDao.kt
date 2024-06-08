package com.syntax.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syntax.domain.entities.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: Account)

    @Query("SELECT * FROM accounts")
    fun getAllAccountNames(): Flow<List<Account>>
    @Delete
    suspend fun deleteAccount(account: Account)
    @Query("UPDATE accounts SET balance = :newBalance WHERE name = :accountName")
    suspend fun updateAccountBalance(accountName: String, newBalance: Double)
}