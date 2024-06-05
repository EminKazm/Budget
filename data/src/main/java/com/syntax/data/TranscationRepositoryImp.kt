package com.syntax.data

import android.util.Log
import androidx.lifecycle.LiveData
import com.syntax.data.database.dao.AccountDao
import com.syntax.data.database.dao.TransactionDao
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : TransactionRepository {
    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions()
    }
    override suspend fun insertTransaction(transaction: Transaction) {
        Log.d("TransactionRepository", "Inserting transaction: $transaction")

        transactionDao.insert(transaction)
    }
    override suspend fun updateAccountBalance(accountName: String, newBalance: Double) {
        accountDao.updateAccountBalance(accountName, newBalance)
    }
    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAll()
    }
    override suspend fun insertAccount(account: Account) {
        accountDao.insert(account)
    }

    override suspend fun getAllAccountNames(): List<Account> {
        return accountDao.getAllAccountNames()
    }
}
