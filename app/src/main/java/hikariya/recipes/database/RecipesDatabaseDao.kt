package hikariya.recipes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipesDatabaseDao {
    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)

    @Query("SELECT * FROM recipe_table WHERE id = :key")
    fun get(key: Long): Recipe?

    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE name = :name")
    fun getByName(name: String): Recipe?

    @Insert
    fun insertIngredient(ingredient: Ingredient)

    @Update
    fun updateIngredient(ingredient: Ingredient)

    @Delete
    fun deleteIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table WHERE recipe_id = :key")
    fun getIngredients(key: Long): List<Ingredient>

    @Query("SELECT DISTINCT LOWER(name) FROM ingredient_table ORDER BY id DESC")
    fun getAllIngredients(): LiveData<List<String>>

    @Query("SELECT * FROM ingredient_table JOIN recipe_table ON ingredient_table.recipe_id = recipe_table.id WHERE LOWER(ingredient_table.name) IN (:nameIngredient)")
    fun getRecipeFilterIngredients(nameIngredient: ArrayList<String>): List<Recipe>
}