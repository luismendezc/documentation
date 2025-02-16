package com.oceloti.lemc.masterapp.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceloti.lemc.masterapp.R
import com.oceloti.lemc.masterapp.databinding.FragmentMasterBinding

class MasterFragment : Fragment() {

  private lateinit var binding: FragmentMasterBinding
  private val viewModel: MasterViewModel by activityViewModels()
  private lateinit var adapter: MenuAdapter
  private var isLandscape = false

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentMasterBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    Log.d("TESTING", "IM IN ON VIEW CREATED")
    isLandscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

    // ✅ Initialize Adapter
    adapter = MenuAdapter { menuItem ->
      viewModel.selectItem(menuItem)

      if (isLandscape) {
        // ✅ 🛑 Fix: Only replace DetailFragment if `detailFragmentContainer` exists
        val detailContainer = requireActivity().findViewById<View>(R.id.detailFragmentContainer)
        if (detailContainer != null) { // ✅ Only replace if in landscape mode
          parentFragmentManager.beginTransaction()
            .replace(R.id.detailFragmentContainer, DetailFragment())
            .commit()
        }
      } else {
        // ✅ Navigate only if we are on the master screen
        val navController = findNavController()
        if (navController.currentDestination?.id == R.id.masterFragment) {
          navController.navigate(R.id.action_masterFragment_to_detailFragment)
        }
      }
    }

    binding.recyclerView.adapter = adapter

    // ✅ Observe menu items and update list
    viewModel.menuItems.observe(viewLifecycleOwner) { items ->
      adapter.submitList(items)
    }
  }
}
