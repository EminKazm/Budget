package com.syntax.reports

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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

        accountAdapter = AccountAdapter()

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
                binding.tvTotalBalanceInUSD.text = "$${totalBalance}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}