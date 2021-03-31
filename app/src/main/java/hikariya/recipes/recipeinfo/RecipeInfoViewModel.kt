package hikariya.recipes.recipeinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*

class RecipeInfoViewModel(val recipeId: Long,
                          val dao: RecipesDatabaseDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var recipe = Recipe()
    var ingredients = ArrayList<Ingredient>()

    private val _shouldBind = MutableLiveData<Boolean>()
    val shouldBind: LiveData<Boolean>
        get() = _shouldBind

    private val _navigateToRecipes = MutableLiveData<Boolean>()
    val navigateToRecipes: LiveData<Boolean>
        get() = _navigateToRecipes

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
        }
    }

    private suspend fun getRecipeDatabase() {
        withContext(Dispatchers.IO) {
            recipe = dao.get(recipeId)!!
        }
    }

    fun onDelete() {
        uiScope.launch {
            delete()
            _navigateToRecipes.value = true
        }
    }

    private suspend fun delete() {
        withContext(Dispatchers.IO) {
            dao.delete(recipe)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}