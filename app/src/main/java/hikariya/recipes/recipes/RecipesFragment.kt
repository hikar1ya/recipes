package hikariya.recipes.recipes

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
        adapter.viewModel = viewModel

        viewModel.recipes.observe(viewLifecycleOwner, Observer { recipesList ->
            if (recipesList != null)
                adapter.data = recipesList
        })

        viewModel.navigateToRecipeInfo.observe(viewLifecycleOwner, Observer { recipe ->
            if (recipe != null) {
                this.findNavController().navigate(
                    RecipesFragmentDirections
                        .actionRecipesFragmentToRecipeInfoFragment(recipe.id)
                )
                viewModel.doneNavigating()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter ->
                this.findNavController().navigate(
                    RecipesFragmentDirections.actionRecipesFragmentToFilterFragment()
                )
        }
        return super.onOptionsItemSelected(item)
    }
}