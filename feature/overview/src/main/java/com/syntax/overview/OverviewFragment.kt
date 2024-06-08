package com.syntax.overview

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syntax.domain.entities.Transaction
import com.syntax.income.R
import com.syntax.income.databinding.FragmentOverviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        transactionAdapter = TransactionAdapter{
            transaction->showDeleteDialog(transaction)
        }

        binding.rvTransactions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = transactionAdapter
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.balance.collect { balance ->
                binding.tvBalance.text = "$${balance}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.expense.collect { expense ->
                binding.tvExpense.text = "$${expense}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.income.collect { income ->
                binding.tvIncome.text = "$${income}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.transactions.collect { transactions ->
                transactionAdapter.submitList(transactions)
            }
        }
        binding.btnReset.setOnClickListener {
            viewModel.resetData()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun showDeleteDialog(transaction: Transaction) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Transaction")
            .setMessage("Are you sure you want to delete this transaction?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteTransaction(transaction)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}