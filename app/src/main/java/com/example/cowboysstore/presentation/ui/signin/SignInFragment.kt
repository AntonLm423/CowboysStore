package com.example.cowboysstore.presentation.ui.signin

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.cowboysstore.R
import com.example.cowboysstore.databinding.FragmentSignInBinding
import com.example.cowboysstore.presentation.ui.catalog.CatalogFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignInFragment : Fragment() {

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
            // имитируем загрузку
            lifecycleScope.launch {  navigateToCatalog() }
        }

        binding.editTextLogin.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                event.action == KeyEvent.ACTION_DOWN &&
                event.keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                lifecycleScope.launch {  navigateToCatalog() }
                true
            } else {
                false
            }
        }
        
    }


   private suspend fun navigateToCatalog() {
       binding.buttonSignIn.isLoading = true
        delay(1000L)
       binding.buttonSignIn.isLoading = false
        parentFragmentManager.commit {
            replace(R.id.containerMain, CatalogFragment())
            addToBackStack(null)
        }
    }

}