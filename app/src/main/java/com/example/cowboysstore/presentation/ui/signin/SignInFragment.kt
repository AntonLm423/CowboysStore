package com.example.cowboysstore.presentation.ui.signin

import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import com.example.cowboysstore.utils.LoadException
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
             validation()
        }

        binding.editTextLogin.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                validation()
                true
            } else {
                false
            }
        }
        
    }

    private fun validation() {
        with(binding) {
            val email = editTextLogin.text.toString()
            val password = editTextPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextLogin.error = "Некорректный адрес электронной почты"
                return
            }

            if (password.length < 6) {
                editTextPassword.error = "Поле содержит менее 8 символов"
                return
            }


            viewModel.authorize(email, password)
            binding.buttonSignIn.isLoading = true
            viewModel.state.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is SignInViewModel.AuthorizationState.Loading -> {
                        binding.buttonSignIn.isLoading = true
                    }
                    is SignInViewModel.AuthorizationState.Success -> {
                        if (state.isAuthorized) {
                            navigateToCatalog()
                        } else {
                            editTextLogin.error = "Неверный логин или пароль"
                            editTextPassword.error = "Неверный логин или пароль"
                        }
                    }
                    is SignInViewModel.AuthorizationState.Error -> {
                       /* TODO() */
                    }
                }
            }

            binding.buttonSignIn.isLoading = false
        }
    }


   private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
            addToBackStack(null)
        }
    }

}