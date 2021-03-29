package hikariya.recipes.addrecipe

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*

class AddRecipeViewModel(
    private val dao: RecipesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateToRecipes = MutableLiveData<Boolean>()
    val navigateToRecipes: LiveData<Boolean>
        get() = _navigateToRecipes

    private val _navigateToAddIngredient = MutableLiveData<Boolean>()
    val navigateToAddIngredient: LiveData<Boolean?>
        get() = _navigateToAddIngredient

    private val _ingrediens = ArrayList<Ingredient>()
    val ingredients : ArrayList<Ingredient>
        get() = _ingrediens

    fun doneNavigating() {
        _navigateToRecipes.value = false
        _navigateToAddIngredient.value = false
    }

    fun onAddIngredient() {
        ingredients.add(Ingredient())
    }

    fun onSave(name: String) {
        uiScope.launch {
            val recipe = Recipe()
            recipe.name = name
            insert(recipe)
            _navigateToRecipes.value = true
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