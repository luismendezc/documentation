package com.oceloti.lemc.masterapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class MasterViewModel : ViewModel() {

  private val _menuItems = MutableLiveData<List<MenuItemData>>()
  val menuItems: LiveData<List<MenuItemData>> = _menuItems

  private val _selectedItem = MutableLiveData<MenuItemData?>()
  val selectedItem: LiveData<MenuItemData?> = _selectedItem

  init {
    _menuItems.value = listOf(
      MenuItemData("Screenshot Capture", "Takes a screenshot"),
      MenuItemData("Item 2", "Description of Item 2")
    )
  }

  fun selectItem(item: MenuItemData) {
    _selectedItem.value = item
  }
}
