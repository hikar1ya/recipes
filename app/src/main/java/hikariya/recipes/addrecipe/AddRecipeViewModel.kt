package hikariya.recipes.addrecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _navigateToRecipes = MutableLiveData<Recipe>()
    val navigateToRecipes: LiveData<Recipe>
        get() = _navigateToRecipes

    fun doneNavigating() {
        _navigateToRecipes.value = null
    }

    fun onSave(name: String) {
        uiScope.launch {
            val recipe = Recipe()
            recipe.name = name
            insert(recipe)
            _navigateToRecipes.value = recipe
        }
    }

    private suspend fun insert(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            dao.insert(recipe)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}