package com.example.cowboysstore.presentation.ui.profile

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.cowboysstore.R
import com.example.cowboysstore.domain.entities.Profile
import com.example.cowboysstore.databinding.FragmentProfileBinding
import com.example.cowboysstore.presentation.adapters.MenuAdapter
import com.example.cowboysstore.presentation.customviews.ProgressContainer
import com.example.cowboysstore.presentation.decorators.SpacingItemDecorator
import com.example.cowboysstore.presentation.ui.orders.OrdersFragment
import com.example.cowboysstore.presentation.ui.settings.SettingsFragment
import com.example.cowboysstore.presentation.ui.signin.SignInFragment
import com.example.cowboysstore.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels()

    private val menuAdapter by lazy { MenuAdapter() }

    private val spacingItemDecorator by lazy {
        SpacingItemDecorator(
            false,
            resources.getDimensionPixelOffset(R.dimen.normal_100)
        )
    }

    private val showSnackBarFlag by lazy {
        requireNotNull(arguments?.getBoolean(Constants.SHOW_SNACK_BAR_KEY, false))
    }

    companion object {
        fun createInstance(showSnackBarFlag: Boolean) = ProfileFragment().apply {
            arguments = bundleOf(Constants.SHOW_SNACK_BAR_KEY to showSnackBarFlag)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        /* Fetching profile details */
        viewModel.loadData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (showSnackBarFlag) {
            showSnackBar(getString(R.string.settings_change_success))
        }

        binding.toolBarProfile.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        initializeMenu()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is ProfileViewModel.ProfileUiState.Loading -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Loading
                        }
                        is ProfileViewModel.ProfileUiState.Success -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Success
                            initializeProfile(uiState.profile, uiState.userPhoto)
                            binding.textViewAppVersion.append(" ${uiState.appVersion}")
                        }
                        is ProfileViewModel.ProfileUiState.Error -> {
                            binding.progressContainerProfile.state = ProgressContainer.State.Notice(
                                uiState.errorResID,
                                uiState.messageResId
                            ) {
                                viewModel.loadData()
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
        }
        menuAdapter.submitList(
            listOf(
                MenuAdapter.MenuItem.Regular(1, getString(R.string.profile_menu_orders), R.drawable.ic_delivery),
                MenuAdapter.MenuItem.Regular(2, getString(R.string.profile_menu_settings), R.drawable.ic_settings),
                MenuAdapter.MenuItem.Red(3, getString(R.string.profile_menu_sign_out), R.drawable.ic_logout)
            )
        )
        menuAdapter.onItemClickListener = {
            when (it) {
                0 -> {
                    navigateToOrders()
                }
                1 -> {
                    navigateToSettings(requireNotNull(viewModel.getProfile()))
                }
                2 -> {
                    showAlertDialog()
                }
            }
        }
    }

    private fun initializeProfile(profile: Profile, userPhoto: Bitmap?) {
        with(binding) {
            textViewUserName.text = "${profile.name} ${profile.surname}"
            textViewUserOccupation.text = profile.occupation
            if (userPhoto == null) {
                imageViewUserAvatar.load(userPhoto) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(16f))
                    error(R.drawable.no_data)
                    placeholder(R.drawable.no_data)
                }
            } else {
                imageViewUserAvatar.setImageBitmap(userPhoto)
            }
        }
    }

    /* Show log out alert dialog */
    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_CowboysStore_AlertDialog)
        builder.setTitle(getString(R.string.profile_exit_dialog_title))
        builder.setPositiveButton(getString(R.string.profile_exit_dialog_positive)) { _, _ ->
            viewModel.clearAccessToken()
            navigateToSignIn()
        }
        builder.setNegativeButton(getString(R.string.profile_exit_dialog_negative)) { dialog, _ ->
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToOrders() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, OrdersFragment())
            addToBackStack(null)
        }
    }

    private fun navigateToSignIn() {
        parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace(R.id.containerMain, SignInFragment())
        }
    }

    private fun navigateToSettings(profile: Profile) {
        parentFragmentManager.commit {
            replace(R.id.containerMain, SettingsFragment())
            addToBackStack(Constants.PROFILE_FRAGMENT_KEY)
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.coordinatorLayoutProfile, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.dark_blue))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }
}
