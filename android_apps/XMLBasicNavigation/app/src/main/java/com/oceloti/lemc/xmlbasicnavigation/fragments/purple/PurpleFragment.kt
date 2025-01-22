package com.oceloti.lemc.xmlbasicnavigation.fragments.purple

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.activities.error.PurpleRoute
import com.oceloti.lemc.xmlbasicnavigation.activities.error.RedRoute
import com.oceloti.lemc.xmlbasicnavigation.databinding.FragmentPurpleBinding

class PurpleFragment : Fragment() {
    private var _binding: FragmentPurpleBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPurpleBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Retrieve and display the RedRoute data
        val routeInfo = findNavController().getBackStackEntry<PurpleRoute>().toRoute<PurpleRoute>()
        binding.textViewPurple.text = "Code: ${routeInfo.id}, Description: ${routeInfo.description}"

        binding.btnMain.setOnClickListener {
            val redRoute = RedRoute(2, "Navigated from PurpleFragment")
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