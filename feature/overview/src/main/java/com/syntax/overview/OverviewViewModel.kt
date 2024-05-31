package com.syntax.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.data.TransactionRepositoryImpl
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _balance = MutableLiveData<Double>()
    val balance: LiveData<Double> get() = _balance

    private val _income = MutableLiveData<Double>()
    val income: LiveData<Double> get() = _income

    private val _expense = MutableLiveData<Double>()
    val expense: LiveData<Double> get() = _expense
    val transactions: LiveData<List<Transaction>> = repository.getAllTransactions()

    init {
        // Call a function to initialize the data after Hilt has injected the repository
        initializeData()
    }

    private fun initializeData() {
        transactions.observeForever {
            calculateBalance(it)
        }
    }

    private fun calculateBalance(transactions: List<Transaction>) {
        val incomeTotal = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val expenseTotal = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
        _income.value = incomeTotal
        _expense.value = expenseTotal
        _balance.value = incomeTotal - expenseTotal
    }
}