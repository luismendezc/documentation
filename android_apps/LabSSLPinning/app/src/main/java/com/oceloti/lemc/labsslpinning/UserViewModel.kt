package com.oceloti.lemc.labsslpinning

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
    // MutableStateFlow for holding the list of users
    private val _usersList = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _usersList

    fun fetchUsers() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUsers()
                if(response.isSuccessful) {
                    response.body()?.let { users ->
                        _usersList.value = users
                        Log.d("UserViewModel", "Users fetched successfully: $users")
                    }
                } else {
                    _usersList.value = emptyList()
                    Log.e("UserViewModel", "Error fetching users: ${response.code()}")
                }
            } catch (e: Exception) {
                ensureActive()
                Log.e("UserViewModel", "Network request failed: ${e}")
                _usersList.value = emptyList()
            }
        }
    }

}