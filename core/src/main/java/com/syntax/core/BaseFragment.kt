package com.syntax.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel<State, Effect, Event>, State, Effect, Event> : Fragment() {

    lateinit var viewModel: VM
    lateinit var binding: VB

    abstract val getViewBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        binding = getViewBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                onStateUpdate(it)
            }
            .launchIn(lifecycleScope)
    }

    abstract fun getViewModelClass(): Class<VM>
    protected open fun onEffectUpdate(effect: Effect) {}


    protected open fun onStateUpdate(state: State) {}

    protected open fun postEvent(event: Event) {
        viewModel.postEvent(event)
    }

}

