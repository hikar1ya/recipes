package hikariya.recipes.addrecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*

class AddRecipeViewModel(
    private val dao: RecipesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun addRecipe(name: String) {
        uiScope.launch {
            val recipe = Recipe()
            recipe.name = name
            insert(recipe)
        }
    }

    private suspend fun insert(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            dao.insert(recipe)
        }
    }
}