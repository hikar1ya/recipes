package hikariya.recipes.editrecipe

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
import hikariya.recipes.addrecipe.AddRecipeFragmentDirections
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.FragmentEditRecipeBinding
import hikariya.recipes.recipeinfo.RecipeInfoFragmentArgs

class EditRecipeFragment : Fragment() {

    private lateinit var viewModel : EditRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_recipe, container, false)

        val application = requireNotNull(this.activity).application
        val args = RecipeInfoFragmentArgs.fromBundle(requireArguments())
        val recipeId = args.recipeId
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = EditRecipeVIewModelFactory(recipeId, dao)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditRecipeViewModel::class.java)

        val adapter = EditRecipeAdapter()
        adapter.viewModel = viewModel
        binding.ingredientsList.adapter = adapter

        binding.editRecipeButton.setOnClickListener {
            viewModel.checkNameDuplicate(binding.nameEditText.text.toString())
            viewModel.duplicateName.observe(viewLifecycleOwner, Observer { duplicate ->
                if (duplicate!!) {
                    binding.nameRecipeWarning.height = 50
                } else {
                    viewModel.onSave(binding.nameEditText.text.toString(), binding.stepsEditText.text.toString())
                    viewModel.onSaveIngredients()
                }
            })
        }

        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigate(
                    AddRecipeFragmentDirections
                        .actionAddRecipeFragmentToRecipesFragment())
            }
        })

        binding.addIngredientButton.setOnClickListener {
            viewModel.ingredients.add(Ingredient())
            adapter.data = viewModel.ingredients
        }

        return binding.root
    }
}