package hikariya.recipes.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel для RecipesFragment
 */
class RecipesViewModel(
    private val dao: RecipesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToRecipeInfo = MutableLiveData<Recipe>()
    val navigateToRecipeInfo: LiveData<Recipe>
        get() = _navigateToRecipeInfo

    val recipes = dao.getAllRecipes()

    fun openRecipeInfo(recipe: Recipe) {
        uiScope.launch {
            _navigateToRecipeInfo.value = recipe
        }
    }

    fun doneNavigating() {
        _navigateToRecipeInfo.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}