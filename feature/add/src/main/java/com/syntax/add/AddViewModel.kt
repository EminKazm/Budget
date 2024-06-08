package com.syntax.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.syntax.data.api.ApiService
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction
import com.syntax.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val currencyApiService: ApiService

) : ViewModel() {
    val accounts: Flow<List<Account>> = repository.getAllAccountNames()

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies: StateFlow<List<String>> get() = _currencies
    init {
        fetchCurrencies()
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun addAccount(accountName: String, currency: String, balance: Double) {
        viewModelScope.launch {
            val account = Account(name = accountName, currency = currency, balance = balance)
            repository.insertAccount(account)
        }
    }

    suspend fun getExchangeRate(fromCurrency: String, toCurrency: String): Double {
        return try {
            currencyApiService.exchangeCurrency(fromCurrency, toCurrency, 1.0)
        } catch (e: Exception) {
            1.0
        }
    }

    fun transferMoney(fromAccount: Account, toAccount: Account, amount: Double, convertedAmount: Double) {
        viewModelScope.launch {
            try {
                repository.updateAccountBalance(fromAccount.name, fromAccount.balance - amount)
                repository.updateAccountBalance(toAccount.name, toAccount.balance + convertedAmount)
            } catch (e: Exception) {
                // Handle error
            }
        }
    }



    fun fetchCurrencies() {
        viewModelScope.launch {
            try {
                val currencies = currencyApiService.getCurrencies()
                _currencies.value = currencies
            } catch (e: Exception) {
                // Handle error case
            }
        }
    }
}