package com.example.lab_testing_coroutines.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_testing_coroutines.R
import com.example.lab_testing_coroutines.databinding.ActivityMainBinding
import com.example.lab_testing_coroutines.logic.auth.AuthState
import com.example.lab_testing_coroutines.logic.auth.AuthState.*
import com.example.lab_testing_coroutines.viewmodels.MainActivityViewModelAbstract
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val _viewModel: MainActivityViewModelAbstract by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _viewModel.actionState.observe(this, this::onStateChanged)

        configureUI()
    }

    private fun configureUI() {
        binding.btnThrowError.setOnClickListener {
            _viewModel.authorize("https://lemc.oceloti.com/inicio/")
        }
        binding.btnScondaryactivity.setOnClickListener {
            startActivity(SecondaryActivity.createIntent(this))
            finish()
        }
    }

    private fun onStateChanged(actionState: AuthState) {
        Log.d(TAG, "Handling state ${actionState}")
        when (actionState) {
            Unauthenticated -> Log.d(TAG, "Not handling on porpouse")
            Auth -> Log.d(TAG, "Authenticating...")
            is DisplayError -> {
                Log.d(TAG, "Got an error now starting activity")
                startActivity(ErrorActivity.createIntent(this, actionState.title, actionState.description))
            }
            else -> {
                Log.d(TAG, "State not recognized")
            }
        }
    }


    companion object{
        private const val TAG = "MainActivity"
    }
}