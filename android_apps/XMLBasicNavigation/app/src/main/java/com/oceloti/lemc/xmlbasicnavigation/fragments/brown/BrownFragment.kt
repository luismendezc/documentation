package com.oceloti.lemc.xmlbasicnavigation.fragments.brown

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.activities.error.BrownRoute
import com.oceloti.lemc.xmlbasicnavigation.activities.error.RedRoute
import com.oceloti.lemc.xmlbasicnavigation.databinding.FragmentBrownBinding

class BrownFragment : Fragment() {

    private var _binding: FragmentBrownBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBrownBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve and display the RedRoute data
        val routeInfo = findNavController().getBackStackEntry<BrownRoute>().toRoute<BrownRoute>()
        binding.textViewBrown.text = "Code: ${routeInfo.id}, Description: ${routeInfo.description}"

        binding.btnMain.setOnClickListener {
            val redRoute = RedRoute(3, "Navigated from BrownFragment")
            findNavController().navigate(route = redRoute,
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