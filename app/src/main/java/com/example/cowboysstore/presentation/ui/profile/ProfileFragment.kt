package com.example.cowboysstore.presentation.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentProfileBinding
import com.example.cowboysstore.presentation.adapters.MenuAdapter
import com.example.cowboysstore.presentation.adapters.MenuItem
import com.example.cowboysstore.presentation.ui.orders.OrdersFragment
import com.example.cowboysstore.utils.getAccessToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel : ProfileViewModel by viewModels()
    private lateinit var binding : FragmentProfileBinding

    private val menuAdapter by lazy { MenuAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel.loadData(getAccessToken(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initializeMenu()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {uiState->
                    when(uiState) {
                        is ProfileViewModel.ProfileUiState.Loading -> {
                          changeVisibility(View.VISIBLE, View.GONE)
                        }
                        is ProfileViewModel.ProfileUiState.Success -> {
                            changeVisibility(View.GONE, View.VISIBLE, uiState.appVersion)

                            with(binding) {
                                textViewUserName.text = "${uiState.profile.name} ${uiState.profile.surname}"
                                textViewUserPosition.text = uiState.profile.occupation
                                imageViewUserAvatar.load(uiState.profile.avatarId) {
                                    crossfade(true)
                                    transformations(RoundedCornersTransformation(16f))
                                    error(R.drawable.no_data)
                                    placeholder(R.drawable.no_data)
                                }
                            }
                        }
                        is ProfileViewModel.ProfileUiState.Error -> {
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
        }
        menuAdapter.submitList(
            listOf(
                MenuItem(1, getString(R.string.profile_menu_orders), R.drawable.ic_delivery),
                MenuItem(1, getString(R.string.profile_menu_settings), R.drawable.ic_settings),
                MenuItem(1, getString(R.string.profile_menu_sign_out), R.drawable.ic_logout)
            )
        )

        menuAdapter.onItemClickListener = {
            when (it) {
                0 -> {
                    navigateToOrders()
                }
                1 -> {
                    Toast.makeText(
                        context,
                        "222",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                2 -> { // TODO: logout dialog
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

    private fun navigateToOrders() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, OrdersFragment())
            addToBackStack(null)
        }
    }

}
