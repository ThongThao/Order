package com.example.order.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Menu
import com.example.order.model.repository.MenuRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val menuRepository = MenuRepository()
    private val _menus = MutableStateFlow<List<Menu>>(emptyList())
    val menus: StateFlow<List<Menu>> = _menus

    init {
        fetchMenus()
    }
    private fun fetchMenus() {
        viewModelScope.launch {
            _menus.value = menuRepository.getMenus()
        }
    }
    fun getMenusForRestaurant(restaurantName: String): StateFlow<List<Menu>> {
        val filteredMenus = MutableStateFlow<List<Menu>>(emptyList())
        viewModelScope.launch {
            _menus.collect { menuList ->
                filteredMenus.value = menuList.filter { it.itemRestaurant == restaurantName }
            }
        }
        return filteredMenus
    }
}