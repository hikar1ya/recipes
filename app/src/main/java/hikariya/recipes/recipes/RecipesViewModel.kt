package hikariya.recipes.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*
import java.util.*

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

    var filtered : List<Recipe> = emptyList()

    private val _filteredRecipes = MutableLiveData<Boolean>()
    val filteredRecipes: LiveData<Boolean>
        get() = _filteredRecipes


    fun openRecipeInfo(recipe: Recipe) {
        uiScope.launch {
            _navigateToRecipeInfo.value = recipe
        }
    }

    fun doneNavigating() {
        _navigateToRecipeInfo.value = null
    }

    fun getFilteredRecipes(array: ArrayList<String>) {
        uiScope.launch {
            getFilteredRecipesDatabase(array)
            _filteredRecipes.value = true
        }
    }

    private suspend fun getFilteredRecipesDatabase(array: ArrayList<String>) {
        withContext(Dispatchers.IO) {
            filtered = dao.getRecipeFilterIngredients(array)
            //filteredRecipes = dao.getRecipeFilterIngredients(array)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}