package hikariya.recipes.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hikariya.recipes.R
import hikariya.recipes.database.Ingredient
import hikariya.recipes.database.RecipesDatabase
import hikariya.recipes.databinding.AddRecipeFragmentBinding

class AddRecipeFragment : Fragment() {

    private lateinit var viewModel: AddRecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: AddRecipeFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.add_recipe_fragment, container, false
        )

        val application = requireNotNull(this.activity).application
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = AddRecipeViewModelFactory(dao, application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AddRecipeViewModel::class.java)

        val adapter = AddRecipeAdapter()
        adapter.viewModel = viewModel
        binding.ingredientsList.adapter = adapter
        adapter.data = viewModel.ingredients

        // добавление нового рецепта
        binding.addRecipe.setOnClickListener {
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

        viewModel.duplicateName.observe(viewLifecycleOwner, Observer { duplicate ->
            if (duplicate!!) {
                binding.nameRecipeWarning.text = "Name already exits"
                binding.nameRecipeWarning.height = 50
            } else {
                binding.nameRecipeWarning.height = 0
            }
        })

        binding.nameEditText.doAfterTextChanged {
            if (binding.nameEditText.text.isNullOrBlank()) {
                binding.nameRecipeWarning.text = "Input name"
                binding.nameRecipeWarning.height = 50
            } else {
                binding.nameRecipeWarning.height = 0
            }
        }

        viewModel.recipeId.observe(viewLifecycleOwner, Observer { id ->
            if (id != -1L) {
                viewModel.onSaveIngredients(id)
            }
        })

        // навигация на главный экран после добавления рецепта
        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            if (shouldNavigate!!) {
                this.findNavController().navigateUp()
                viewModel.doneNavigating()
            }
        })

        // добавление нового ингредиента
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