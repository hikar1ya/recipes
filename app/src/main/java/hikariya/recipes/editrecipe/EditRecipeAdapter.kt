package hikariya.recipes.editrecipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R
import hikariya.recipes.database.Ingredient

class EditRecipeViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameIngredient: EditText = itemView.findViewById(R.id.nameIngredientEditText)
    private val quantityIngredient: EditText = itemView.findViewById(R.id.quantityIngredientEditText)
    val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)

    fun bind(item: Ingredient) {
        nameIngredient.setText(item.name)
        nameIngredient.doAfterTextChanged { item.name = nameIngredient.text.toString() }
        quantityIngredient.setText(item.quantity)
        nameIngredient.doAfterTextChanged { item.quantity = quantityIngredient.text.toString() }
    }

    companion object {
        fun from(parent: ViewGroup): EditRecipeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_ingredient_add, parent, false)
            return EditRecipeViewHolder(view)
        }
    }
}

class EditRecipeAdapter : RecyclerView.Adapter<EditRecipeViewHolder>() {

    var data = arrayListOf<Ingredient>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var viewModel: EditRecipeViewModel

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: EditRecipeViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.buttonDelete.setOnClickListener {
            viewModel.ingredients.remove(item)
            data = viewModel.ingredients
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditRecipeViewHolder {
        return EditRecipeViewHolder.from(parent)
    }
}