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
import hikariya.recipes.Data
import hikariya.recipes.R
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.FragmentEditRecipeBinding

class EditRecipeFragment : Fragment() {

    private lateinit var viewModel: EditRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEditRecipeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_recipe, container, false
        )

        val application = requireNotNull(this.activity).application
        val recipeId = Data.recipeId
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = EditRecipeVIewModelFactory(recipeId, dao)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditRecipeViewModel::class.java)

        val adapter = EditRecipeAdapter()
        adapter.viewModel = viewModel
        binding.ingredientsList.adapter = adapter
        adapter.data = viewModel.ingredients

        viewModel.shouldBind.observe(viewLifecycleOwner, Observer { should ->
            if (should!!) {
                binding.nameEditText.setText(viewModel.recipe.name)
                adapter.data = viewModel.ingredients
                binding.stepsEditText.setText(viewModel.recipe.steps)
            }
        })

        binding.editRecipeButton.setOnClickListener {
            if (binding.nameEditText.text.isEmpty()) {
                binding.nameRecipeWarning.text = "Input name"
                binding.nameRecipeWarning.height = 50
            } else {
                if (isIngredientsEmpty()) {
                    binding.ingredientsWarning.height = 50
                } else {
                    binding.ingredientsWarning.height = 0
                    viewModel.checkNameDuplicate(binding.nameEditText.text.toString())
                }
            }
        }

        viewModel.canSave.observe(viewLifecycleOwner, Observer { canSave ->
            if (canSave!!) {
                viewModel.onSave(
                    binding.nameEditText.text.toString(),
                    binding.stepsEditText.text.toString()
                )
            }
        })

        viewModel.navigateToRecipeInfo.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigateUp()
                viewModel.doneNavigation()
            }
        })

        binding.addIngredientButton.setOnClickListener {
            if (binding.columnNameTextView.height == 0) {
                binding.columnNameTextView.height = 60
                binding.columnQuantityTextView.height = 60
            }
            viewModel.ingredients.add(Ingredient())
            adapter.data = viewModel.ingredients
        }

        return binding.root
    }

    private fun isIngredientsEmpty(): Boolean {
        viewModel.ingredients.forEach { ingredient ->
            if (ingredient.name.isEmpty() || ingredient.quantity.isEmpty())
                return true
        }
        return false
    }
}