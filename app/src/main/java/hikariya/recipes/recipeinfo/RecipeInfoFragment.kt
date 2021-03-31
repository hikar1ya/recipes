package hikariya.recipes.recipeinfo

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
import hikariya.recipes.databinding.FragmentRecipeInfoBinding


class RecipeInfoFragment : Fragment() {

    private lateinit var viewModel: RecipeInfoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRecipeInfoBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_recipe_info, container, false
        )

        val application = requireNotNull(this.activity).application
        val args = RecipeInfoFragmentArgs.fromBundle(requireArguments())
        val recipeId = args.recipeId
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = RecipeInfoViewModelFactory(recipeId, dao)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RecipeInfoViewModel::class.java)

        viewModel.getRecipe(recipeId)

        viewModel.shouldBind.observe(viewLifecycleOwner, Observer { should ->
            if (should!!) {
                val ingredients : String

                binding.recipeNameInfo.text = viewModel.recipe.name
                binding.ingredientsInfoButton.setOnClickListener {
                    binding.contentText?.text = "Ингредиенты"
                }

                binding.cookingInfoButton.setOnClickListener {
                    binding.contentText?.text = viewModel.recipe.steps
                }
            }

        })
        return binding.root
    }
}