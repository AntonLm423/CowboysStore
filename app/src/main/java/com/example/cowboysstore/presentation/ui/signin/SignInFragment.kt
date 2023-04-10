package com.example.cowboysstore.presentation.ui.signin

import android.os.Bundle
import android.util.Patterns
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
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

   private val viewModel : SignInViewModel by viewModels()
   private lateinit var binding : FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      binding = FragmentSignInBinding.inflate(inflater,container, false)
      return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignIn.setOnClickListener {
            navigateToCatalog()
        }

        /*binding.buttonSignIn.setOnClickListener {
             if(validation()) {
                 authorization()
             }
        }

        binding.editTextLogin.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                if(validation()) {
                    authorization()
                }
                true
            } else {
                false
            }
        }*/
        
    }

    private fun validation() : Boolean {
        with(binding) {
            val email = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextLogin.error = getString(R.string.sign_in_validation_mail_failed)
                return false
            }

            if (password.length < 6) {
                editTextPassword.error = getString(R.string.sign_in_validation_password_failed)
                return false
            }

          return true
        }
    }

    private fun authorization() {
        with(binding) {

            binding.buttonSignIn.isLoading = true

           

            viewModel.authorize(
                editTextLogin.text.toString(),
                editTextPassword.text.toString()
            )


        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SignInViewModel.AuthorizationState.Loading -> {
                    binding.buttonSignIn.isLoading = true
                }
                is SignInViewModel.AuthorizationState.Success -> {
                    if (state.isAuthorized) {
                        navigateToCatalog()
                    } else {
                        editTextLogin.error = getString(R.string.sign_in_auth_invalid)
                        editTextPassword.error = getString(R.string.sign_in_auth_invalid)
                        binding.buttonSignIn.isLoading = false
                    }
                }
                is SignInViewModel.AuthorizationState.Error -> {
                    Toast.makeText(
                        context,
                        getString(R.string.sign_in_auth_error),
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.buttonSignIn.isLoading = false
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