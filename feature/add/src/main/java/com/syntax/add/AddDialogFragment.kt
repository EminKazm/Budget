package com.syntax.add

import android.app.Dialog
import android.os.Bundle
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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.syntax.domain.entities.Account
import com.syntax.transfers.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDialogFragment : BottomSheetDialogFragment() {

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_dialog, container, false)

        val etAccountName = view.findViewById<EditText>(R.id.et_account_name)
        val spCurrency = view.findViewById<Spinner>(R.id.sp_currency)
        val balance = view.findViewById<EditText>(R.id.et_balance)
        val btnAddAccount = view.findViewById<Button>(R.id.btn_add_account)

        lifecycleScope.launch {
            viewModel.currencies.collect { currencies ->
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, currencies)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spCurrency.adapter = adapter
            }
        }

        btnAddAccount.setOnClickListener {
            val accountName = etAccountName.text.toString()
            val balanceValue = balance.text.toString().toDoubleOrNull() ?: 0.0
            val currency = spCurrency.selectedItem.toString()
            if (accountName.isNotEmpty()) {
                viewModel.addAccount(accountName, currency, balanceValue)
                dismiss()
            }
        }

        return view
    }

    override fun onStart() {
        super.onStart()

        // Get the bottom sheet view
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

        // Get the behavior of the bottom sheet
        val behavior = bottomSheet?.let { BottomSheetBehavior.from(it) }

        // Set the peek height to half of the screen height
        val screenHeight = requireContext().resources.displayMetrics.heightPixels
        behavior?.peekHeight = screenHeight / 2

        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
}
