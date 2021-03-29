package hikariya.recipes.addrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R
import hikariya.recipes.database.Ingredient

class AddRecipeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameIngredient: EditText = itemView.findViewById(R.id.nameIngredientEditText)
    val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

    fun bind(item: Ingredient) {
        nameIngredient.setText(item.name)
    }

    companion object {
        fun from(parent: ViewGroup): AddRecipeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_ingredient, parent, false)
            return AddRecipeViewHolder(view)
        }
    }
}

class AddRecipeAdapter : RecyclerView.Adapter<AddRecipeViewHolder>() {

    var data = arrayListOf<Ingredient>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var viewModel: AddRecipeViewModel

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AddRecipeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.buttonDelete.setOnClickListener {
            val items = data
            items.remove(item)
            data = items
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRecipeViewHolder {
        return AddRecipeViewHolder.from(parent)
    }
}