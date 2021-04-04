package hikariya.recipes.filter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import hikariya.recipes.database.RecipesDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class FilterViewModel (
    private val dao: RecipesDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val ingredients = dao.getAllIngredients()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}