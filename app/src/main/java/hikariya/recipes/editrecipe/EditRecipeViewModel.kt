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
    var ingredientsIds = ArrayList<Long>()

    private val _duplicateName = MutableLiveData<Boolean>()
    val duplicateName: LiveData<Boolean>
        get() = _duplicateName

    private val _navigateToRecipeInfo = MutableLiveData<Boolean>()
    val navigateToRecipeInfo: LiveData<Boolean>
        get() = _navigateToRecipeInfo

    private val _shouldBind = MutableLiveData<Boolean>()
    val shouldBind: LiveData<Boolean>
        get() = _shouldBind

    private val _canSave = MutableLiveData<Boolean>()
    val canSave: LiveData<Boolean>
        get() = _canSave

    init {
        initializeRecipe()
    }

    private fun initializeRecipe() {
        uiScope.launch {
            getRecipeDatabase()
            getIngredientsDatabase()
            _shouldBind.value = true
        }
    }

    private suspend fun getIngredientsDatabase() {
        withContext(Dispatchers.IO) {
            ingredients = dao.getIngredients(recipeId) as ArrayList<Ingredient>
            ingredients.map { i -> i.id }.also { ingredientsIds = it as ArrayList<Long> }
        }
    }

    private suspend fun getRecipeDatabase() {
        withContext(Dispatchers.IO) {
            recipe = dao.get(recipeId)!!
        }
    }

    fun checkNameDuplicate(name: String) {
        uiScope.launch {
            val dupRecipe = getByName(name)
            val duplicate = (dupRecipe != null) && (name != recipe.name)
            _duplicateName.value = duplicate
            _canSave.value = !duplicate
        }
    }

    fun onSave(name: String, steps: String) {
        uiScope.launch {
            recipe.name = name
            recipe.steps = steps
            update(recipe)
            onSaveIngredients()
        }
    }

    private suspend fun update(recipe: Recipe) {
        withContext(Dispatchers.IO) {
            dao.update(recipe)
        }
    }

    private fun onSaveIngredients() {
        uiScope.launch {
            ingredients.forEach { i ->
                if (ingredientsIds.contains(i.id)) {
                    ingredientsIds.remove(i.id)
                    updateIngredient(i)
                } else {
                    i.recipe_id = recipeId
                    insertIngredient(i)
                }
            }
            ingredientsIds.forEach { i ->
                val ingredient = Ingredient()
                ingredient.id = i
                removeIngredient(ingredient)
            }
            _navigateToRecipeInfo.value = true
        }
    }

    private suspend fun updateIngredient(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            dao.updateIngredient(ingredient)
        }
    }

    private suspend fun removeIngredient(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            dao.deleteIngredient(ingredient)
        }
    }

    private suspend fun getByName(name: String): Recipe? {
        return withContext(Dispatchers.IO) {
            dao.getByName(name)
        }
    }

    private suspend fun insertIngredient(ingredient: Ingredient) {
        withContext(Dispatchers.IO) {
            dao.insertIngredient(ingredient)
        }
    }

    fun doneNavigation() {
        _navigateToRecipeInfo.value = false
    }
}