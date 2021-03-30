package hikariya.recipes.recipeinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R

class RecipeInfoViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val text: TextView = itemView.findViewById(R.id.text)

    fun bind(item: String) {
        text.text = "a"
    }

    companion object {
        fun from(parent: ViewGroup): RecipeInfoViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.fragment_recipe_info_tab_item, parent, false)
            return RecipeInfoViewHolder(view)
        }
    }
}

class RecipeInfoAdapter : RecyclerView.Adapter<RecipeInfoViewHolder>() {

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var viewModel: RecipeInfoViewModel

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: RecipeInfoViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeInfoViewHolder {
        return RecipeInfoViewHolder.from(parent)
    }
}