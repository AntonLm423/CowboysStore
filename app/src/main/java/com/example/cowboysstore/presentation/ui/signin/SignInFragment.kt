package com.example.cowboysstore.presentation.ui.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import com.example.cowboysstore.utils.Constants
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

        /*-------------------------------------------------*/
        binding.editTextLogin.setText("qwerty@mail.ru")
        binding.editTextPassword.setText("12345678")
        /*-------------------------------------------------*/

        binding.buttonSignIn.setOnClickListener {
            if(!viewModel.validate(
                    // TODO: Remake validation function
                    binding.editTextLogin.text.toString(),
                    binding.editTextPassword.text.toString()
            )
            ) {
                binding.editTextLogin.error = getString(R.string.sign_in_validation_error)
            }
            else {
                viewModel.authorize(
                    binding.editTextLogin.text.toString(),
                    binding.editTextPassword.text.toString()
                )

                viewModel.state.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is SignInViewModel.AuthorizationState.Loading -> {
                            binding.buttonSignIn.isLoading = true
                        }
                      is SignInViewModel.AuthorizationState.Success -> {
                          binding.buttonSignIn.isLoading = false
                          val sharedPref = requireContext().getSharedPreferences(Constants.PREFS_KEY, Context.MODE_PRIVATE)
                          val editor = sharedPref.edit()
                          editor.putString(Constants.PREFS_AUTH_KEY, "Bearer ${state.accessToken}")
                          editor.apply()
                          navigateToCatalog()
                      }
                        is SignInViewModel.AuthorizationState.Error -> {
                            binding.buttonSignIn.isLoading = false
                            // TODO: ERROR DIALOG
                            Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        /*binding.editTextLogin.setOnEditorActionListener { _, actionId, event ->
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

   private fun navigateToCatalog() {
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
            addToBackStack(null)
        }
    }
}