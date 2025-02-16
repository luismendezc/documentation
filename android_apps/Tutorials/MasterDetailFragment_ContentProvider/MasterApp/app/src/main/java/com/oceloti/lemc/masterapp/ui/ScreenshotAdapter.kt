package com.oceloti.lemc.masterapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceloti.lemc.masterapp.databinding.ItemScreenshotBinding
import java.io.File

class ScreenshotAdapter : ListAdapter<File, ScreenshotAdapter.ViewHolder>(DiffCallback) {

  class ViewHolder(private val binding: ItemScreenshotBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(file: File) {
      binding.textViewScreenshotName.text = file.name
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemScreenshotBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val file = getItem(position)
    holder.bind(file)
  }

  companion object {
    private val DiffCallback = object : DiffUtil.ItemCallback<File>() {
      override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem.absolutePath == newItem.absolutePath
      }

      override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
        return oldItem == newItem
      }
    }
  }
}
