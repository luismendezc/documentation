package com.oceloti.lemc.xmlbasicnavigation.activities.nativesplash

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.activities.main.MainActivity
import com.oceloti.lemc.xmlbasicnavigation.databinding.ActivityNativeSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NativeSplash : AppCompatActivity() {
    private lateinit var binding: ActivityNativeSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNativeSplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lifecycleScope.launch {
            delay(3000)
            startActivity(MainActivity.createIntent(this@NativeSplash))
            finish()
        }
    }
}