package hikariya.recipes.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * ViewModel для RecipesFragment
 */
class RecipesViewModel(
    private val dao: RecipesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val recipes = dao.getAllRecipes()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}