package hikariya.recipes.recipeinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R
import hikariya.recipes.database.Ingredient

class RecipeInfoViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.nameIngredientTextView)
    private val quantity: TextView = itemView.findViewById(R.id.quantityIngredientTextView)

    fun bind(item: Ingredient) {
        name.text = item.name
        quantity.text = item.quantity
    }

    companion object {
        fun from(parent: ViewGroup): RecipeInfoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_ingredient, parent, false)
            return RecipeInfoViewHolder(view)
        }
    }
}

class RecipeInfoAdapter : RecyclerView.Adapter<RecipeInfoViewHolder>() {

    var data = listOf<Ingredient>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecipeInfoViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeInfoViewHolder {
        return RecipeInfoViewHolder.from(parent)
    }
}