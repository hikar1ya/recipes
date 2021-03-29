package hikariya.recipes.addrecipe

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hikariya.recipes.database.RecipesDatabaseDao

@Suppress("UNCHECKED_CAST")
class AddRecipeViewModelFactory(
    private val dao: RecipesDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddRecipeViewModel::class.java)) {
            return AddRecipeViewModel(dao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}