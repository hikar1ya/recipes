package hikariya.recipes.recipeinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.*

class RecipeInfoViewModel(val recipeId: Long,
                          val dao: RecipesDatabaseDao
) : ViewModel() {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _shouldBind = MutableLiveData<Boolean>()
    val shouldBind: LiveData<Boolean>
        get() = _shouldBind

    var recipe = Recipe()

    fun getRecipe(id: Long) {
        uiScope.launch {
            onGetRecipe(id)
        }
    }

    private suspend fun onGetRecipe(id: Long) {
        withContext(Dispatchers.IO) {
            recipe = dao.get(id)!!
        }
        _shouldBind.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}