package com.example.cowboysstore.presentation.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.databinding.FragmentProfileBinding
import com.example.cowboysstore.presentation.adapters.MenuAdapter
import com.example.cowboysstore.presentation.adapters.MenuItem
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.RoundedItemDecorator
import com.example.cowboysstore.presentation.decorators.SpacingItemDecorator
import com.example.cowboysstore.presentation.ui.orders.OrdersFragment
import com.example.cowboysstore.presentation.ui.signin.SignInFragment
import com.example.cowboysstore.utils.Constants
import com.example.cowboysstore.utils.getAccessToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel : ProfileViewModel by viewModels()
    private lateinit var binding : FragmentProfileBinding

    private val menuAdapter by lazy { MenuAdapter() }

    private val spacingItemDecorator by lazy { SpacingItemDecorator(false, resources.getDimensionPixelOffset(R.dimen.normal_100)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        /* Fetching profile details */
        viewModel.loadData(getAccessToken(requireContext()))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initializeMenu()

       viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState->
                    when(uiState) {
                        is ProfileViewModel.ProfileUiState.Loading -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Loading
                        }
                        is ProfileViewModel.ProfileUiState.Success -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Success
                            initializeProfile(uiState.profile)
                            binding.textViewAppVersion.append(" ${uiState.appVersion}")
                        }
                        is ProfileViewModel.ProfileUiState.Error -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Notice(
                               uiState.errorResID,
                                uiState.messageResId
                            ) {
                                viewModel.loadData(getAccessToken(requireContext()))
                            }
                        }
                    }
                }
            }
        }

    }

    /* Initializing RecyclerViewMenu with 3 items:
    * Orders;
    * Settings;
    * Sign out.
    */
    private fun initializeMenu() {

        binding.recyclerViewMenu.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = menuAdapter
            addItemDecoration(spacingItemDecorator)
            addItemDecoration(RoundedItemDecorator(16f))
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
                    // TODO: Profile settings Fragment
                }
                2 -> {
                    // TODO: Logout dialog
                    val prefs = requireContext().getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE)
                    prefs.edit().clear().apply()
                    navigateToSignIn()
                }
            }
        }
    }

    private fun initializeProfile(profile : Profile) {
        with(binding) {
            textViewUserName.text = "${profile.name} ${profile.surname}"
            textViewUserPosition.text = profile.occupation
            imageViewUserAvatar.load(profile.avatarId) {
                crossfade(true)
                transformations(RoundedCornersTransformation(16f))
                error(R.drawable.no_data)
                placeholder(R.drawable.no_data)
            }
        }
    }

    private fun navigateToOrders() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, OrdersFragment())
            addToBackStack(null)
        }
    }

    private fun navigateToSignIn() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, SignInFragment())
        }
    }

}
