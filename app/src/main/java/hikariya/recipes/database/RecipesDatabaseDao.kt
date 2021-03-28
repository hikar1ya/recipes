package hikariya.recipes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipesDatabaseDao {
    @Insert
    fun insert(recipe: Recipe)

    @Update
    fun update(recipe: Recipe)

    @Query("SELECT * FROM recipe_table WHERE id = :key")
    fun get(key: Long): Recipe?

    @Query("SELECT * FROM recipe_table ORDER BY id DESC")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe_table WHERE name LIKE :name")
    fun getByName(name: String): Recipe?

    @Insert
    fun insertIngredient(ingredient: Ingredient)

    @Update
    fun updateIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient_table WHERE recipe_id = :key")
    fun getIngredients(key: Long): LiveData<List<Ingredient>>
}