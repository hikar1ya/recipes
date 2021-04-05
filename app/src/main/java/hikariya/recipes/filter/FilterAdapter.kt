package hikariya.recipes.filter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hikariya.recipes.R

class FilterViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.ingredientTextView)
    private val checkBox: CheckBox = itemView.findViewById(R.id.checkbox)

    fun bind(item: String, viewModel: FilterViewModel) {
        name.text = item
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.selectedIngredients.add(item)
            } else {
                viewModel.selectedIngredients.remove(item)
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): FilterViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.list_item_ingredient_filter, parent, false)
            return FilterViewHolder(view)
        }
    }
}

class FilterAdapter : RecyclerView.Adapter<FilterViewHolder>() {

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var viewModel: FilterViewModel

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, viewModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        return FilterViewHolder.from(parent)
    }
}