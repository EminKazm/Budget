package com.syntax.add

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.syntax.transfers.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferMoneyDialog : BottomSheetDialogFragment() {

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_transfer_money, null as ViewGroup?)

        val spFromAccount = view.findViewById<Spinner>(R.id.sp_from_account)
        val spToAccount = view.findViewById<Spinner>(R.id.sp_to_account)
        val etAmount = view.findViewById<EditText>(R.id.amount)
        val btnTransfer = view.findViewById<Button>(R.id.btn_transfer)

        lifecycleScope.launch {
            viewModel.accounts.collect { accounts ->
                val accountNames = accounts.map { it.name }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    accountNames
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spFromAccount.adapter = adapter
                spToAccount.adapter = adapter
            }
        }

        btnTransfer.setOnClickListener {
            val fromAccountName = spFromAccount.selectedItem.toString()
            val toAccountName = spToAccount.selectedItem.toString()
            val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0

            if (fromAccountName.isNotEmpty() && toAccountName.isNotEmpty() && amount > 0) {
                lifecycleScope.launch {
                    val accounts = viewModel.accounts.first()
                    val fromAccount = accounts.find { it.name == fromAccountName }
                    val toAccount = accounts.find { it.name == toAccountName }

                    if (fromAccount != null && toAccount != null) {
                        viewModel.transferMoney(fromAccount, toAccount, amount)
                        dismiss()
                    }
                }
            }
        }

        return Dialog(requireContext()).apply {
            setContentView(view)
            setTitle("Transfer Money")
        }
    }

}