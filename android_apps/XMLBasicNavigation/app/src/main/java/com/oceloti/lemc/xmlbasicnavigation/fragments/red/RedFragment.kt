package com.oceloti.lemc.xmlbasicnavigation.fragments.red

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.activities.error.BrownRoute
import com.oceloti.lemc.xmlbasicnavigation.activities.error.PurpleRoute
import com.oceloti.lemc.xmlbasicnavigation.activities.error.RedRoute
import com.oceloti.lemc.xmlbasicnavigation.databinding.FragmentRedBinding

class RedFragment : Fragment() {
    private var _binding: FragmentRedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRedBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve and display the RedRoute data
        val routeRoute = findNavController().getBackStackEntry<RedRoute>().toRoute<RedRoute>()
        binding.textViewRed.text = "Code: ${routeRoute.id}, Description: ${routeRoute.description}"

        binding.btnMain.setOnClickListener {
            val purpleRoute = PurpleRoute(404, "Navigated from RedFragment")
            findNavController().navigate(route = purpleRoute,
                navOptions {
                    popUpToRoute
                })
        }

        binding.btnSecond.setOnClickListener {
            val brownRoute = BrownRoute(401, "Navigated from RedFragment")
            findNavController().navigate(route = brownRoute,
                navOptions {
                    popUpToRoute
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}