package com.example.cowboysstore.presentation.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.cowboysstore.R
import com.example.cowboysstore.data.model.Profile
import com.example.cowboysstore.databinding.FragmentSettingsBinding
import com.example.cowboysstore.presentation.dialogs.SelectDialog
import com.example.cowboysstore.presentation.ui.profile.ProfileFragment
import com.example.cowboysstore.utils.Constants
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    private val selectOccupationDialog by lazy { SelectDialog(requireContext(), proposedOccupations) }
    private val selectImageSourceDialog by lazy { SelectDialog(requireContext(), proposedImageSources) }

    private val proposedImageSources by lazy {
        listOf(
            getString(R.string.settings_image_source_camera),
            getString(R.string.settings_image_source_gallery)
        )
    }

    private val proposedOccupations by lazy {
        listOf(
            getString(R.string.settings_occupation_developer),
            getString(R.string.settings_occupation_tester),
            getString(R.string.settings_occupation_manager),
            getString(R.string.settings_occupation_other)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel.loadProfile()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    when (uiState) {
                        is SettingsViewModel.SettingsUiState.Success -> {
                            initializeFields(uiState.profile)
                        }
                        is SettingsViewModel.SettingsUiState.Empty -> {
                            // do nothing, fields has empty state by default
                        }
                    }
                }
            }
        }
    }

    private fun initializeFields(profile: Profile) = with(binding) {
        /* Text fields */
        editTextName.setText(profile.name)
        editTextSurname.setText(profile.surname)
        autoCompleteTextViewOccupation.setText(profile.occupation)

        /* Click listeners */
        binding.toolbarSettings.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        imageViewCamera.setOnClickListener {
            selectImageSourceDialog.show()
        }

        autoCompleteTextViewOccupation.setOnClickListener {
            selectOccupationDialog.show()
        }

        buttonApplyChanges.setOnClickListener {
            changeProfile(profile)
        }

        /* Selection image source */
        selectImageSourceDialog.selectingItemAdapter.itemClickListener = { selectedItem ->
            selectImageSourceDialog.hide()
            when (selectedItem) {
                proposedImageSources[0] -> {

                }
                proposedImageSources[1] -> {

                }
            }
        }

        /* Selection occupation */
        selectOccupationDialog.selectingItemAdapter.itemClickListener = { selectedItem ->
            autoCompleteTextViewOccupation.setText(selectedItem)
            selectOccupationDialog.hide()
            when (selectedItem) {
                proposedOccupations.last() -> {
                    textInputLayoutOtherOccupation.visibility = View.VISIBLE
                }
                else -> {
                    textInputLayoutOtherOccupation.visibility = View.GONE
                }
            }
        }
    }

    private fun changeProfile(profile: Profile) = with(binding) {
        val newProfile = profile.copy()
        newProfile.name = editTextName.text.toString()
        newProfile.surname = editTextSurname.text.toString()

        newProfile.occupation =
            if (autoCompleteTextViewOccupation.text.toString() == getString(R.string.settings_occupation_other)) editTextOtherOccupation.text.toString() else autoCompleteTextViewOccupation.text.toString()

        viewModel.changeProfile(profile, newProfile)
        buttonApplyChanges.isLoading = true

        viewModel.profileEditResult.observe(viewLifecycleOwner) { result ->
            if (result) {
                buttonApplyChanges.isLoading = false
                navigateToProfile()
            } else {
                showSnackBar(getString(R.string.settings_change_error))
                buttonApplyChanges.isLoading = false
            }
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(binding.linearLayoutSettings, message, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red_input_failure))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }

    private fun navigateToProfile() {
        parentFragmentManager.popBackStack(Constants.PROFILE_FRAGMENT_KEY, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        parentFragmentManager.commit {
            replace(R.id.containerMain, ProfileFragment.createInstance(true))
        }
    }
}