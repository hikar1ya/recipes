package hikariya.recipes.editrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import hikariya.recipes.database.RecipesDatabaseDao

@Suppress("UNCHECKED_CAST")
class EditRecipeVIewModelFactory(
    private val recipeId: Long,
    private val dao: RecipesDatabaseDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditRecipeViewModel::class.java)) {
            return EditRecipeViewModel(recipeId, dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}