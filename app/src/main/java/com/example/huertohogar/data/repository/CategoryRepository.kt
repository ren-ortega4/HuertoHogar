package com.example.huertohogar.data.repository

import com.example.huertohogar.data.local.CategoryDao
import com.example.huertohogar.model.CategoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoryRepository(private val categoryDao: CategoryDao) {

    val allCategories: Flow<List<CategoryEntity>> = categoryDao.getAllCategories()

    suspend fun populateDatabaseIfEmpty(){
        withContext(Dispatchers.IO) {
            if (categoryDao.getCategoryCount() == 0) {
                categoryDao.insertAll(getInitialCategories())
            }
        }
    }

    private fun getInitialCategories(): List<CategoryEntity>{
        return listOf(
            CategoryEntity(
                name = "Frutas Frescas",
                imageResName = "fruta",
                description = "Nuestra selección de frutas frescas ofrece una experiencia directa del campo a \n" +
                        "tu hogar. Estas frutas se cultivan y cosechan en el punto óptimo de madurez para asegurar \n" +
                        "su sabor y frescura. Disfruta de una variedad de frutas de temporada que aportan vitaminas \n" +
                        "y nutrientes esenciales a tu dieta diaria. Perfectas para consumir solas, en ensaladas o como \n" +
                        "ingrediente principal en postres y smoothies."
            ),
            CategoryEntity(
                name = "Verduras Orgánicas",
                imageResName = "verdura",
                description = "Descubre nuestra gama de verduras orgánicas, cultivadas sin el uso de \n" +
                        "pesticidas ni químicos, garantizando un sabor auténtico y natural. Cada verdura es \n" +
                        "seleccionada por su calidad y valor nutricional, ofreciendo una excelente fuente de \n" +
                        "vitaminas, minerales y fibra. Ideales para ensaladas, guisos y platos saludables, nuestras \n" +
                        "verduras orgánicas promueven una alimentación consciente y sostenible. "
            ),
            CategoryEntity(
                name = "Productos Orgánicos",
                imageResName = "organico",
                description = "Nuestros productos orgánicos están elaborados con ingredientes naturales y \n" +
                        "procesados de manera responsable para mantener sus beneficios saludables. Desde aceites \n" +
                        "y miel hasta granos y semillas, ofrecemos una selección que apoya un estilo de vida \n" +
                        "saludable y respetuoso con el medio ambiente. Estos productos son perfectos para quienes \n" +
                        "buscan opciones alimenticias que aporten bienestar sin comprometer el sabor ni la calidad. "
            ),
            CategoryEntity(
                name = "Productos Lácteos",
                imageResName = "lacteos",
                description = "Los productos lácteos de HuertoHogar provienen de granjas locales que se \n" +
                        "dedican a la producción responsable y de calidad. Ofrecemos una gama de leches, yogures \n" +
                        "y otros derivados que conservan su frescura y sabor auténtico. Ricos en calcio y nutrientes \n" +
                        "esenciales, nuestros lácteos son perfectos para complementar una dieta equilibrada, \n" +
                        "proporcionando el mejor sabor y nutrición para toda la familia. "
            )
        )
    }

}