package com.example.cowboysstore.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.fragment.app.commit
import androidx.fragment.app.add
import com.example.cowboysstore.R
import com.example.cowboysstore.presentation.ui.signin.SignInFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fitContentViewToInsets()

        supportFragmentManager.commit {
            add<SignInFragment>(R.id.containerMain)
        }
    }

    private fun fitContentViewToInsets() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}