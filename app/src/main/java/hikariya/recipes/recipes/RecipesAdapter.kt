package hikariya.recipes.recipes

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R
import hikariya.recipes.database.Recipe

class RecipesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameRecipe: TextView = itemView.findViewById(R.id.name_recipe)

    fun bind(item: Recipe) {
        nameRecipe.text = item.name
    }

    companion object {
        fun from(parent: ViewGroup): RecipesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_recipe, parent, false)
            return RecipesViewHolder(view)
        }
    }
}

class RecipesAdapter : RecyclerView.Adapter<RecipesViewHolder>() {

    var data = listOf<Recipe>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        return RecipesViewHolder.from(parent)
    }
}