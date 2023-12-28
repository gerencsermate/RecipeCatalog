package hu.bme.aut.android.recipeapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe WHERE category = :cat")
    fun getRecipesFromCategory(cat: Category) : List<Recipe>

    @Insert
    fun insert(recipe: Recipe) : Long

    @Update
    fun update(recipe: Recipe)

    @Delete
    fun delete(recipe: Recipe)
}