package com.example.cowboysstore.presentation.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentOrdersBinding
import com.example.cowboysstore.presentation.adapters.PagerTabAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding : FragmentOrdersBinding
    private val viewModel : OrdersViewModel by viewModels()

    private lateinit var pagerTabAdapter: PagerTabAdapter

    private lateinit var tabNames : List<String>
    private val fragmentList = listOf(
        OrdersListFragment.createInstance(false),
        OrdersListFragment.createInstance(true)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarOrders.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        tabNames = listOf(
            getString(R.string.orders_tab_all),
            getString(R.string.orders_tab_active)
        )

        // TODO : Handling states
        binding.progressContainerOrders.state = ProgressContainer.State.Success

        pagerTabAdapter = PagerTabAdapter(this, fragmentList)

        binding.viewPagerTabs.apply {
            adapter = pagerTabAdapter
        }

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPagerTabs,
        ) { tab, pos ->
            tab.text = tabNames[pos]
        }.attach()
    }
}