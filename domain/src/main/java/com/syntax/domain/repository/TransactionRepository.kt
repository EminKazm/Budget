package com.syntax.domain.repository

import androidx.lifecycle.LiveData
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
    suspend fun insertAccount(account: Account)
    fun getAllAccountNames():Flow<List<Account>>
    suspend fun updateAccountBalance(accountName: String, newBalance: Double)


}
