package hikariya.recipes.addrecipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.navigation.fragment.findNavController
import hikariya.recipes.R
import hikariya.recipes.database.Recipe
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.database.RecipesDatabaseDao
import hikariya.recipes.databinding.AddRecipeFragmentBinding
import hikariya.recipes.databinding.FragmentRecipesBinding
import hikariya.recipes.recipes.RecipesAdapter
import hikariya.recipes.recipes.RecipesViewModel
import hikariya.recipes.recipes.RecipesViewModelFactory

class AddRecipeFragment : Fragment() {

    private lateinit var viewModel: AddRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddRecipeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_recipe_fragment, container, false)

        val application = requireNotNull(this.activity).application
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = AddRecipeViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AddRecipeViewModel::class.java)

        // добавление нового рецепта
        binding.addRecipe.setOnClickListener {
            viewModel.onSave(binding.nameEditText.text.toString())
        }

        //навигация на главный экран после добавления рецепта
        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer { recipe ->
            if (recipe != null) {
                this.findNavController().navigate(
                    AddRecipeFragmentDirections
                        .actionAddRecipeFragmentToRecipesFragment())
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }

}