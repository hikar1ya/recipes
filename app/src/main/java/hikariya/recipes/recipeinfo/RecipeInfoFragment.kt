package hikariya.recipes.recipeinfo

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hikariya.recipes.Data
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
        val recipeId = Data.recipeId
        val dao = RecipesDatabase.getInstance(application).getRecipesDatabaseDao()
        val viewModelFactory = RecipeInfoViewModelFactory(recipeId, dao)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RecipeInfoViewModel::class.java)

        val adapter = RecipeInfoAdapter()
        binding.contentIngredients.adapter = adapter

        viewModel.shouldBind.observe(viewLifecycleOwner, Observer { should ->
            if (should!!) {
                binding.recipeNameInfo.text = viewModel.recipe.name
                binding.contentText.text = viewModel.recipe.steps
                adapter.data = viewModel.ingredients
                binding.ingredientsInfoButton.setOnClickListener {
                    binding.contentText.visibility = View.INVISIBLE
                    binding.contentIngredients.visibility = View.VISIBLE
                }

                binding.cookingInfoButton.setOnClickListener {
                    binding.contentText.visibility = View.VISIBLE
                    binding.contentIngredients.visibility = View.INVISIBLE
                }
            }

        })

        viewModel.navigateToRecipes.observe(viewLifecycleOwner, Observer { should ->
            if (should!!) {
                this.findNavController().navigate(
                    RecipeInfoFragmentDirections
                        .actionRecipeInfoFragmentToRecipesFragment()
                )
                viewModel.doneNavigating()
            }
        })

        viewModel.navigateToEditRecipe.observe(viewLifecycleOwner, Observer { should ->
            if (should!!) {
                this.findNavController().navigate(
                    RecipeInfoFragmentDirections
                        .actionRecipeInfoFragmentToEditRecipeFragment()
                )
                viewModel.doneNavigating()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.recipe_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete -> viewModel.onDelete()
            R.id.edit -> viewModel.onEdit()
        }
        return super.onOptionsItemSelected(item)
    }
}