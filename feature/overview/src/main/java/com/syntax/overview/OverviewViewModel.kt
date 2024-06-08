package com.syntax.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.data.TransactionRepositoryImpl
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _balance = MutableStateFlow<Double>(0.0)
    val balance: StateFlow<Double> get() = _balance

    private val _income = MutableStateFlow<Double>(0.0)
    val income: StateFlow<Double> get() = _income

    private val _expense = MutableStateFlow<Double>(0.0)
    val expense: StateFlow<Double> get() = _expense
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions
    init {
        loadTransactions()
    }
    private fun loadTransactions(){
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactionList ->
                _transactions.value = transactionList
                calculateBalance(transactionList)
            }
        }
    }
    private fun calculateBalance(transactions: List<Transaction>) {
        Log.d("OverviewViewModel", "Transactions updated: $transactions")

        val incomeTotal = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val expenseTotal = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
        _income.value = incomeTotal
        _expense.value = expenseTotal
        _balance.value = incomeTotal - expenseTotal
    }
    fun resetData() {
        viewModelScope.launch {
            repository.deleteAllTransactions()
            _transactions.value = emptyList()
            _balance.value = 0.0
            _income.value = 0.0
            _expense.value = 0.0
        }
    }
    fun deleteTransaction(transactions: Transaction){
        viewModelScope.launch {
            repository.deleteTransaction(transactions)
            loadTransactions()
        }
    }
}