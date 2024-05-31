package com.syntax.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }
}