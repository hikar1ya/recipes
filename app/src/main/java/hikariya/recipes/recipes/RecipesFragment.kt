package hikariya.recipes.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hikariya.recipes.R
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.FragmentRecipesBinding

class RecipesFragment : Fragment() {

    private lateinit var viewModel: RecipesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRecipesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipes, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = RecipesViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RecipesViewModel::class.java)

        val adapter = RecipesAdapter()
        binding.recipesList.adapter = adapter

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipesList ->
            if (recipesList != null)
                adapter.data = recipesList
        })

        return binding.root
    }
}