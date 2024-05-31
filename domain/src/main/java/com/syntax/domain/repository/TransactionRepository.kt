package com.syntax.domain.repository

import androidx.lifecycle.LiveData
import com.syntax.domain.entities.Transaction

interface TransactionRepository {
    fun getAllTransactions(): LiveData<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)

}
