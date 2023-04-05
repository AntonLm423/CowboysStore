package com.example.cowboysstore.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentProfileBinding
import com.example.cowboysstore.presentation.adapters.MenuAdapter
import com.example.cowboysstore.presentation.adapters.MenuItem
import com.example.cowboysstore.presentation.decorators.RoundedCornersDecoration
import com.example.cowboysstore.presentation.decorators.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel : ProfileViewModel by viewModels()
    private lateinit var binding : FragmentProfileBinding

    private val menuAdapter by lazy { MenuAdapter() }
    private val itemSpacing by lazy { SpacingItemDecoration(16) }
    private val itemRounder by lazy { RoundedCornersDecoration(8f)}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMenu()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {uiState->
                    when(uiState) {
                        is ProfileUiState.Loading -> {
                          changeVisibility(View.VISIBLE, View.GONE)
                        }
                        is ProfileUiState.Success -> {
                            changeVisibility(View.GONE, View.VISIBLE, uiState.appVersion)
                        }
                        is ProfileUiState.Error -> {
                            changeVisibility(View.GONE, View.VISIBLE, getString(uiState.errorResID))
                        }
                    }
                }
            }
        }

    }


    private fun initializeMenu() {

        binding.recyclerViewMenu.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = menuAdapter
            addItemDecoration(itemSpacing)
        }
        menuAdapter.submitList(
            listOf(
                MenuItem(1, getString(R.string.profile_menu_orders), R.drawable.ic_my_orders),
                MenuItem(1, getString(R.string.profile_menu_settings), R.drawable.ic_settings),
                MenuItem(1, getString(R.string.profile_menu_sign_out), R.drawable.ic_logout)
            )
        )

        menuAdapter.onItemClickListener = {
            when (it) {
                0 -> {
                    Toast.makeText(
                        context,
                        "111",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                1 -> {
                    Toast.makeText(
                        context,
                        "222",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                2 -> {
                    Toast.makeText(
                        context,
                        "333",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun changeVisibility(
        pbVisibility : Int,
        tvVisibility : Int,
        tvText : String? = null
    ) {
        with(binding) {
            progressBasAppVersion.visibility = pbVisibility
            textViewAppVersion.visibility = tvVisibility
            tvText?.let {
               textViewAppVersion.append(" $tvText")
            }
        }
    }

}
