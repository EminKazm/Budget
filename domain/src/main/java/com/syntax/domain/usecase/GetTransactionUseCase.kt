package com.syntax.domain.usecase

import androidx.lifecycle.LiveData
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    fun execute(): Flow<List<Transaction>> {
        return transactionRepository.getAllTransactions()
    }
}