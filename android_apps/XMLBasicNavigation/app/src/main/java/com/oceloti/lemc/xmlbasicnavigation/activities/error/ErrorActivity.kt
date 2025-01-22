package com.oceloti.lemc.xmlbasicnavigation.activities.error

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.databinding.ActivityErrorBinding
import com.oceloti.lemc.xmlbasicnavigation.fragments.brown.BrownFragment
import com.oceloti.lemc.xmlbasicnavigation.fragments.purple.PurpleFragment
import com.oceloti.lemc.xmlbasicnavigation.fragments.red.RedFragment
import kotlinx.serialization.Serializable

class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityErrorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.graph = navController.createGraph(startDestination = RedRoute(1, "Initial Red Fragment")) {
            fragment<RedFragment, RedRoute> {
                label = "Red Fragment"
            }
            // Purple Fragment
            fragment<PurpleFragment, PurpleRoute> {
                label = "Purple Fragment"
            }

            // Brown Fragment
            fragment<BrownFragment, BrownRoute> {
                label = "Brown Fragment"
            }
        }
    }
    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ErrorActivity::class.java)
        }
    }
}

@Serializable
data class RedRoute(val id: Int, val description: String)
@Serializable
data class PurpleRoute(val id: Int, val description: String)
@Serializable
data class BrownRoute(val id: Int, val description: String)