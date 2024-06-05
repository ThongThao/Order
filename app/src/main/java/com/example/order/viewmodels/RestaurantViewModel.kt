package com.example.order.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Restaurant
import com.example.order.model.repository.RestaurantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel : ViewModel() {
    private val repository: RestaurantRepository = RestaurantRepository()
    private val restaurantRepository = RestaurantRepository()
    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants: StateFlow<List<Restaurant>> = _restaurants

    init {
        fetchRestaurants()
    }

    private fun fetchRestaurants() {
        viewModelScope.launch {
            _restaurants.value = restaurantRepository.getRestaurants()
        }
    }
    fun getRestaurantById(restaurantId: String?): StateFlow<Restaurant?> {
        val restaurantFlow = MutableStateFlow<Restaurant?>(null)
        viewModelScope.launch {
            repository.getRestaurantById(restaurantId).collect { restaurant ->
                restaurantFlow.value = restaurant
            }
        }
        return restaurantFlow
    }


}