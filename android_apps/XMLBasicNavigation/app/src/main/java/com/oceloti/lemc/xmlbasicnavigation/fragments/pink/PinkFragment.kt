package com.oceloti.lemc.xmlbasicnavigation.fragments.pink

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.activities.error.ErrorActivity
import com.oceloti.lemc.xmlbasicnavigation.databinding.FragmentPinkBinding

class PinkFragment : Fragment() {
    private var _binding: FragmentPinkBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPinkBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMain.setOnClickListener {
            findNavController().navigate(R.id.action_pinkFragment_to_secondNavGraph)
        }
        binding.btnSecond.setOnClickListener {
            startActivity(ErrorActivity.createIntent(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}