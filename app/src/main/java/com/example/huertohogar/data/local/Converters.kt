package com.example.huertohogar.data.local

import androidx.room.TypeConverter
import com.example.huertohogar.model.ProductCategory

class Converters {

    @TypeConverter
    fun fromProductCategory(category: ProductCategory): String {
        return category.name
    }

    @TypeConverter
    fun toProductCategory(categoryString: String): ProductCategory {
        return ProductCategory.valueOf(categoryString)
    }
}