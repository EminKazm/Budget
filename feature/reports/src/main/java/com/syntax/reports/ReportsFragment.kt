package com.syntax.reports

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.syntax.domain.entities.Account
import com.syntax.domain.entities.Transaction
import com.syntax.reports.databinding.FragmentReportsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportsFragment : Fragment() {
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReportsViewModel by viewModels()
    private lateinit var accountAdapter: AccountAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountAdapter = AccountAdapter { account -> showDeleteDialog(account) }

        binding.rvAccounts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = accountAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.accountsReport.collect { accounts ->
                accountAdapter.submitList(accounts)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.totalBalanceInUSD.collect { totalBalance ->
                binding.tvTotalBalanceInUSD.text = "Net worth in USD: $${totalBalance}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.incomeReport.collect { totalIncome ->
                binding.tvIncomeReport.text = "Total Income: $${totalIncome}"
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.outcomeReport.collect { totalOutcome ->
                binding.tvOutcomeReport.text = "Total Outcome: $${totalOutcome}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDeleteDialog(account: Account) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to delete this account?")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.deleteAccount(account)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}