package com.oceloti.lemc.masterapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.oceloti.lemc.masterapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

  private lateinit var binding: FragmentDetailBinding
  private val detailViewModel: DetailViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    binding.buttonTakeScreenshot.setOnClickListener {
      detailViewModel.takeScreenshot(binding.root, requireContext()) { file ->
        if (file != null) {
          Toast.makeText(requireContext(), "Screenshot saved: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
        } else {
          Toast.makeText(requireContext(), "Failed to take screenshot", Toast.LENGTH_SHORT).show()
        }
      }
    }

    // Setup RecyclerView
    val adapter = ScreenshotAdapter()
    binding.recyclerViewScreenshots.layoutManager = LinearLayoutManager(requireContext())
    binding.recyclerViewScreenshots.adapter = adapter

    detailViewModel.screenshots.observe(viewLifecycleOwner) { screenshots ->
      adapter.submitList(screenshots)

      // Show/hide UI elements based on availability
      if (screenshots.isNotEmpty()) {
        binding.textViewScreenshots.visibility = View.VISIBLE
        binding.recyclerViewScreenshots.visibility = View.VISIBLE
      } else {
        binding.textViewScreenshots.visibility = View.GONE
        binding.recyclerViewScreenshots.visibility = View.GONE
      }
    }

    // Load existing screenshots
    detailViewModel.loadScreenshots(requireContext())
  }
}
