package hikariya.recipes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Recipe::class, Ingredient::class], version = 1, exportSchema = false)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun getRecipesDatabaseDao(): RecipesDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: RecipesDatabase? = null

        fun getInstance(context: Context): RecipesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(context.applicationContext,
                        RecipesDatabase::class.java, "recipes_db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}