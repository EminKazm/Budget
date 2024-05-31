package com.syntax.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.syntax.income.R
import com.syntax.income.databinding.FragmentOverviewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OverviewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.balance.observe(viewLifecycleOwner, {
            binding.tvBalance.text = "$${it}"
        })

        viewModel.income.observe(viewLifecycleOwner, {
            binding.tvIncome.text = "$${it}"
        })

        viewModel.expense.observe(viewLifecycleOwner, {
            binding.tvExpense.text = "$${it}"
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}