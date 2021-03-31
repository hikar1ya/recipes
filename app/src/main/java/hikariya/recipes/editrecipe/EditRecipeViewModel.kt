package hikariya.recipes.editrecipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*

class EditRecipeViewModel (
    val recipeId: Long,
    val dao: RecipesDatabaseDao
    ) : ViewModel() {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipe = Recipe()

    var ingredients = ArrayList<Ingredient>()

    private val _duplicateName = MutableLiveData<Boolean>()
    val duplicateName: LiveData<Boolean>
        get() = _duplicateName

    private val _navigateToRecipes = MutableLiveData<Boolean>()
    val navigateToRecipes: LiveData<Boolean>
        get() = _navigateToRecipes

    init {
        initializeRecipe()
    }

    private fun initializeRecipe() {
        uiScope.launch {
            getRecipeDatabase()
        }
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

    fun onSave(name: String, steps: String) {
        uiScope.launch {
            val recipe = Recipe()
            recipe.name = name
            recipe.steps = steps
            update(recipe)
        }
    }

    private suspend fun update(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            dao.update(recipe)
        }
    }

    fun onSaveIngredients() {
        uiScope.launch {
            ingredients.forEach { i -> i.recipe_id = recipeId }
            ingredients.forEach { i ->
                updateIngredient(i)
            }
            _navigateToRecipes.value = true
        }
    }

    private suspend fun updateIngredient(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            dao.insertIngredient(ingredient)
        }
    }

    private suspend fun getByName(name: String): Recipe? {
        return withContext(Dispatchers.IO) {
            dao.getByName(name)
        }
    }

    private suspend fun getRecipeDatabase() {
        withContext(Dispatchers.IO) {
            recipe = dao.get(recipeId)!!
        }
    }
}