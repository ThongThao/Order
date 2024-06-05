package com.example.order.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.order.model.Category
import com.example.order.model.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val categoryRepository = CategoryRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getCategories()
        }
    }


}