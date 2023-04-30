package com.example.cowboysstore.presentation.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.cowboysstore.R
import com.example.cowboysstore.data.local.prefs.Preferences
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
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
    private fun tryLogIn() {
        val username = binding.editTextLogin.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (!viewModel.validate(username, password)) {
            binding.editTextLogin.error = getString(R.string.sign_in_validation_error)
            binding.editTextPassword.error = getString(R.string.sign_in_validation_error)
        } else {
            viewModel.authorize(username, password)

            viewModel.authorizationState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is SignInViewModel.AuthorizationState.Loading -> {
                        binding.buttonSignIn.isLoading = true
                    }
                    is SignInViewModel.AuthorizationState.Success -> {
                        binding.buttonSignIn.isLoading = false
                        viewModel.saveAccessToken(state.accessToken)
                        navigateToCatalog()
                    }
                    is SignInViewModel.AuthorizationState.Error -> {
                        binding.buttonSignIn.isLoading = false
                        // TODO: Bottom Sheet Dialog
                        Toast.makeText(requireContext(), getString(state.errorResId), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
            addToBackStack(null)
        }
    }
}