package com.oceloti.lemc.xmlbasicnavigation.fragments.orange

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.oceloti.lemc.xmlbasicnavigation.R
import com.oceloti.lemc.xmlbasicnavigation.databinding.FragmentOrangeBinding

class OrangeFragment : Fragment() {

    private var _binding: FragmentOrangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =FragmentOrangeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMain.setOnClickListener {
            findNavController().navigate(R.id.action_orangeFragment_to_blueFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}