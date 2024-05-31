package com.syntax.data

import androidx.lifecycle.LiveData
import com.syntax.data.database.dao.TransactionDao
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getAllTransactions(): LiveData<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }
    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insert(transaction)
    }
}
