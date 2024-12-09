package com.example.lab_testing_coroutines.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab_testing_coroutines.R
import com.example.lab_testing_coroutines.databinding.ActivityErrorBinding
import com.example.lab_testing_coroutines.databinding.ActivityMainBinding

class ErrorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvTitle.text = intent.getStringExtra(KEY_DISPLAY_TITLE)
        binding.tvDescription.text = intent.getStringExtra(KEY_DISPLAY_TEXT)
    }

    companion object {
        const val KEY_DISPLAY_TITLE = "KEY_DISPLAY_TITLE"
        const val KEY_DISPLAY_TEXT = "KEY_DISPLAY_TEXT"

        fun createIntent(
            context: Context,
            displayTitle: String? = null,
            displayText: String? = null,
        ): Intent {
            val intent = Intent(context, ErrorActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            displayTitle?.let { intent.putExtra(KEY_DISPLAY_TITLE, it) }
            displayText?.let { intent.putExtra(KEY_DISPLAY_TEXT, it) }
            return intent
        }
    }

}