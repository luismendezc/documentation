package com.oceloti.lemc.masterapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.oceloti.lemc.masterapp.databinding.ItemMenuBinding

class MenuAdapter(private val onItemClick: (MenuItemData) -> Unit) :
  ListAdapter<MenuItemData, MenuAdapter.ViewHolder>(DiffCallback) {

  class ViewHolder(private val binding: ItemMenuBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MenuItemData, onItemClick: (MenuItemData) -> Unit) {
      binding.textViewTitle.text = item.title
      binding.root.setOnClickListener { onItemClick(item) }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    holder.bind(item, onItemClick)
  }

  companion object {
    private val DiffCallback = object : DiffUtil.ItemCallback<MenuItemData>() {
      override fun areItemsTheSame(oldItem: MenuItemData, newItem: MenuItemData): Boolean {
        return oldItem.title == newItem.title
      }

      override fun areContentsTheSame(oldItem: MenuItemData, newItem: MenuItemData): Boolean {
        return oldItem == newItem
      }
    }
  }
}
