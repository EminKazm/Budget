package com.syntax.add

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.syntax.domain.entities.Account
import com.syntax.transfers.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDialogFragment : DialogFragment() {

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_add_dialog, null as ViewGroup?)

        val etAccountName = view.findViewById<EditText>(R.id.et_account_name)
        val spCurrency = view.findViewById<Spinner>(R.id.sp_currency)
        val btnAddAccount = view.findViewById<Button>(R.id.btn_add_account)

        viewModel.currencies.observe(this) { currencies ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCurrency.adapter = adapter
        }

        btnAddAccount.setOnClickListener {
            val accountName = etAccountName.text.toString()
            val currency = spCurrency.selectedItem.toString()
            if (accountName.isNotEmpty()) {
                val account = Account(id=id ,name = accountName, currency = currency)
                viewModel.addAccount(account)
                dismiss()
            }
        }

        return Dialog(requireContext()).apply {
            setContentView(view)
            setTitle("Add Account")
        }
    }
}
