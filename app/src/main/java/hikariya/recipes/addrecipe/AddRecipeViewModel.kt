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

    private val _duplicateName = MutableLiveData<Boolean>()
    val duplicateName: LiveData<Boolean>
        get() = _duplicateName

    var ingredients = ArrayList<Ingredient>()

    private val _recipeId = MutableLiveData<Long>()
    val recipeId: LiveData<Long>
        get() = _recipeId

    fun doneNavigating() {
        _navigateToRecipes.value = false
    }

    fun checkNameDuplicate(name: String) {
        uiScope.launch {
            val recipe = getByName(name)
            val duplicate = recipe != null
            if (duplicate != _duplicateName.value) {
                _duplicateName.value = duplicate
            }
        }
    }

    private suspend fun getByName(name: String): Recipe? {
        return withContext(Dispatchers.IO) {
            dao.getByName(name)
        }
    }

    fun onSave(name: String, steps: String) {
        uiScope.launch {
            val recipe = Recipe()
            recipe.name = name
            recipe.steps = steps
            insert(recipe)
            initializeRecipeId(name)
        }
    }

    fun onSaveIngredients(id: Long) {
        uiScope.launch {
            ingredients.forEach { i -> i.recipe_id = id }
            ingredients.forEach { i ->
                insertIngredient(i)
            }
            _navigateToRecipes.value = true
        }
    }

    private fun initializeRecipeId(name: String) {
        uiScope.launch {
            _recipeId.value = getIdRecipeFromDatabase(name)
        }
    }

    private suspend fun getIdRecipeFromDatabase(name: String): Long {
        return withContext(Dispatchers.IO) {
            dao.getByName(name)?.id
        }!!
    }

    private suspend fun insertIngredient(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            dao.insertIngredient(ingredient)
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