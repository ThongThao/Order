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
    fun getTopRatedRestaurants(): StateFlow<List<Restaurant>> {
        val topRatedRestaurantsFlow = MutableStateFlow<List<Restaurant>>(emptyList())
        viewModelScope.launch {
            val allRestaurants = _restaurants.value
            if (allRestaurants.isNotEmpty()) {
                // Sắp xếp danh sách theo restaurantRate giảm dần
                val sortedRestaurants = allRestaurants.sorted()
                // Lấy 3 nhà hàng có restaurantRate cao nhất
                val top3Restaurants = sortedRestaurants.take(3)
                topRatedRestaurantsFlow.value = top3Restaurants
            }
        }
        return topRatedRestaurantsFlow
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