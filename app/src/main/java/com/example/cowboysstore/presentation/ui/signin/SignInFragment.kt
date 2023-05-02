package com.example.cowboysstore.presentation.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.cowboysstore.R
import com.example.cowboysstore.data.local.prefs.Preferences
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import com.example.cowboysstore.utils.Validator
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment()
: Fragment() {

   private val viewModel : SignInViewModel by viewModels()
   private lateinit var binding : FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentSignInBinding.inflate(inflater,container, false)



   /* if(checkAuthToken()) {
          navigateToCatalog()
      }*/

      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /************************************************/
        binding.editTextLogin.setText("qwerty@mail.ru")
        binding.editTextPassword.setText("12345678")
        /************************************************/

        binding.buttonSignIn.setOnClickListener {
           tryLogIn()
        }

        binding.editTextPassword.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                tryLogIn()
                true
            }
           else {
                false
            }
        }
    }

    /* Log in by email and password */
    private fun tryLogIn() = with(binding) {
        textInputLayoutLogin.error = null
        textInputLayoutPassword.error = null

        val email = editTextLogin.text.toString()
        val password = editTextPassword.text.toString()

        when {
            !Validator.isEmailValid(email) && !Validator.isPasswordValid(password) -> {
                textInputLayoutLogin.error = getString(R.string.sign_in_validation_error)
                textInputLayoutPassword.error = getString(R.string.sign_in_validation_error)
            }
            !Validator.isEmailValid(email) -> {
                textInputLayoutLogin.error = getString(R.string.sign_in_validation_error)
            }
            !Validator.isPasswordValid(password) -> {
                textInputLayoutPassword.error = getString(R.string.sign_in_validation_error)
            }
            else -> {
                viewModel.authorize(email, password)
                viewModel.authorizationState.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is SignInViewModel.AuthorizationState.Loading -> {
                            buttonSignIn.isLoading = true
                        }
                        is SignInViewModel.AuthorizationState.Success -> {
                            buttonSignIn.isLoading = false
                            viewModel.saveAccessToken(state.accessToken)
                            navigateToCatalog()
                        }
                        is SignInViewModel.AuthorizationState.Error -> {
                            buttonSignIn.isLoading = false
                            showErrorSnackbar(state.errorResId)
                        }
                    }
                }
            }
        }
    }

    private fun showErrorSnackbar(errorResId: Int) {
        val snackBar = Snackbar.make(binding.frameLayoutSignIn, errorResId, Snackbar.LENGTH_LONG)
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red_input_failure))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }

    private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
            addToBackStack(null)
        }
    }
}