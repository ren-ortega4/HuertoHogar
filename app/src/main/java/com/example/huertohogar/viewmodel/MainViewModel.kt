package com.example.huertohogar.viewmodel

import android.app.Application
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.R
import com.example.huertohogar.data.CategoryEntity
import com.example.huertohogar.data.TipDatabase
import com.example.huertohogar.model.Product
import com.example.huertohogar.repository.CategoryRepository
import com.example.huertohogar.repository.ProductRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainScreenUiState(
    val featuredProducts: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isLoading: Boolean = true
)


data class Category(
    val name: String,
    @DrawableRes val imageRes: Int,
    val description: String
)


class MainViewModel(application: Application) : AndroidViewModel(application) {

    val uiState: StateFlow<MainScreenUiState>

    init {
        val categoryDao = TipDatabase.getDatabase(application).categoryDao()
        val categoryRepository = CategoryRepository(categoryDao)

        viewModelScope.launch {
            categoryRepository.populateDatabaseIfEmpty()
        }

        val categoriesFromDbFlow = categoryRepository.allCategories
            .map { entities ->
                entities.map { entity ->
                    entity.toCategoryModel(getApplication())
                }
            }

        uiState = combine(
            categoriesFromDbFlow,
            ProductRepository.getProductsFlow()
        ) { categoryList, productList ->
            MainScreenUiState(
                featuredProducts = productList,
                categories = categoryList,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainScreenUiState(isLoading = true)
        )
    }
}

fun CategoryEntity.toCategoryModel(context: Context): Category {
    val resourceId = context.resources.getIdentifier(this.imageResName, "drawable", context.packageName)

    return Category(
        name = this.name,
        imageRes = if (resourceId != 0) resourceId else R.drawable.fondooscuro,
        description = this.description
    )
}