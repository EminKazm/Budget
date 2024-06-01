package com.syntax.add

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.fragment.app.viewModels
import com.syntax.domain.entities.Transaction
import com.syntax.transfers.R
import com.syntax.transfers.databinding.FragmentAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rgTransactionType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_income, R.id.rb_expense -> binding.llTransferDetails.visibility = View.GONE
                R.id.rb_transfer -> binding.llTransferDetails.visibility = View.VISIBLE
            }
        }

        binding.btnAddTransaction.setOnClickListener {
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.etCategory.text.toString()
            val selectedType = view?.findViewById<RadioButton>(binding.rgTransactionType.checkedRadioButtonId)?.text.toString()

            val transaction = Transaction(
                id = 0, // Auto-generate ID
                amount = amount,
                category = category,
                type = selectedType
            )

            viewModel.addTransaction(transaction)
        }

        binding.btnAddAccount.setOnClickListener {
            // Logic to add new account
            val fromAccount = binding.etFromAccount.text.toString()
            val toAccount = binding.etToAccount.text.toString()
            val currency = binding.etCurrency.text.toString()

            viewModel.addAccount(fromAccount, toAccount, currency)
        }
        viewModel.currencies.observe(viewLifecycleOwner) { currencies ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                currencies
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.etCurrency.setAdapter(adapter)
        }

        viewModel.fetchCurrencies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}