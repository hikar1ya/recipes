package hikariya.recipes.recipeinfo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hikariya.recipes.database.RecipesDatabaseDao

@Suppress("UNCHECKED_CAST")
class RecipeInfoViewModelFactory(
    private val recipeId: Long,
    private val dao: RecipesDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeInfoViewModel::class.java)) {
            return RecipeInfoViewModel(recipeId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}