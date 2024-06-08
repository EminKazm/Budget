package com.syntax.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.data.api.ApiService
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val currencyApiService: ApiService
) : ViewModel() {

    private val _accountsReport = MutableStateFlow<List<Account>>(emptyList())
    val accountsReport: StateFlow<List<Account>> = _accountsReport

    private val _totalBalanceInUSD = MutableStateFlow(0.0)
    val totalBalanceInUSD: StateFlow<Double> = _totalBalanceInUSD

    private val _incomeReport = MutableStateFlow(0.0)
    val incomeReport: StateFlow<Double> = _incomeReport

    private val _outcomeReport = MutableStateFlow(0.0)
    val outcomeReport: StateFlow<Double> = _outcomeReport

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            repository.getAllAccountNames().collect { accounts ->
                _accountsReport.value = accounts
                calculateTotalBalanceInUSD(accounts)
            }
        }
        viewModelScope.launch {
            repository.getAllTransactions().collect { transactions ->
                calculateIncomeOutcome(transactions)
            }
        }
    }

    private fun calculateTotalBalanceInUSD(accounts: List<Account>) {
        viewModelScope.launch {
            try {
                val totalBalance = accounts.sumOf { account ->
                    if (account.currency != "USD") {
                        val exchangeRate = currencyApiService.exchangeCurrency(account.currency, "USD", account.balance)
                        account.balance * exchangeRate
                    } else {
                        account.balance
                    }
                }
                _totalBalanceInUSD.value = totalBalance
            } catch (e: Exception) {
                _totalBalanceInUSD.value = 0.0
            }
        }
    }

    private fun calculateIncomeOutcome(transactions: List<Transaction>) {
        val income = transactions.filter { it.type == "Income" }.sumOf { it.amount }
        val outcome = transactions.filter { it.type == "Expense" }.sumOf { it.amount }
        _incomeReport.value = income
        _outcomeReport.value = outcome
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }
}