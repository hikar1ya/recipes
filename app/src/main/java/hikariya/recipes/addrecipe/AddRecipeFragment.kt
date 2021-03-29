package hikariya.recipes.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hikariya.recipes.R
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.AddRecipeFragmentBinding

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

        val adapter = AddRecipeAdapter()
        binding.ingredientsList.adapter = adapter


        // добавление нового рецепта
        binding.addRecipe.setOnClickListener {
            viewModel.onSave(binding.nameEditText.text.toString())
        }

        // навигация на главный экран после добавления рецепта
        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    AddRecipeFragmentDirections
                        .actionAddRecipeFragmentToRecipesFragment())
                viewModel.doneNavigating()
            }
        })

        // добавление нового ингредиента
        binding.addIngredientButton.setOnClickListener {
            viewModel.onAddIngredient()
            adapter.data = viewModel.ingredients
        }


        return binding.root
    }

}