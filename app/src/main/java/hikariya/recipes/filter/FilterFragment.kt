package hikariya.recipes.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hikariya.recipes.R
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.FragmentFilterBinding
import hikariya.recipes.recipeinfo.RecipeInfoAdapter

class FilterFragment : Fragment() {

    private lateinit var viewModel: FilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFilterBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_filter, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = FilterViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(FilterViewModel::class.java)

        val adapter = RecipeInfoAdapter()
        binding.ingredientsList.adapter = adapter

        viewModel.ingredients.observe(viewLifecycleOwner, Observer { ingredientsList ->
            if (ingredientsList != null)
                adapter.data = ingredientsList
        })

        return binding.root
    }
}