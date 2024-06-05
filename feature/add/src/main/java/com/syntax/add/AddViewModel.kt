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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val currencyApiService: ApiService

) : ViewModel() {
    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> get() = _accounts

    private val _currencies = MutableLiveData<List<String>>()
    val currencies: LiveData<List<String>> get() = _currencies
    init {
        loadAccounts()
        fetchCurrencies()
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
        }
    }

    fun addAccount(account: Account) {
        viewModelScope.launch {
            repository.insertAccount(account)
            loadAccounts()  // Refresh the account list
        }
    }

    fun transferMoney(fromAccount: Account, toAccount: Account, amount: Double) {
        viewModelScope.launch {
            try {
                val exchangeRate = if (fromAccount.currency != toAccount.currency) {
                    currencyApiService.exchangeCurrency(fromAccount.currency, toAccount.currency, amount)
                } else {
                    1.0
                }
                val convertedAmount = amount * exchangeRate
                // Update accounts' balances accordingly
                repository.updateAccountBalance(fromAccount.name, fromAccount.balance - amount)
                repository.updateAccountBalance(toAccount.name, toAccount.balance + convertedAmount)
                loadAccounts()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    private fun loadAccounts() {
        viewModelScope.launch {
            _accounts.value = repository.getAllAccountNames()
        }
    }

    fun fetchCurrencies() {
        viewModelScope.launch {
            try {
                val currencies = currencyApiService.getCurrencies()
                _currencies.postValue(currencies)
            } catch (e: Exception) {
                // Handle error case
            }
        }
    }
}