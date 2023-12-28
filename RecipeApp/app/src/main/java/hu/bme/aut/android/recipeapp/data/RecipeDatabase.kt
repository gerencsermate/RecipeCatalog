package hu.bme.aut.android.recipeapp.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Recipe::class], version = 6)
@TypeConverters(value = [hu.bme.aut.android.recipeapp.data.TypeConverters::class])
abstract class RecipeDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao

    companion object{
        fun getDatabase(applicationContext: Context): RecipeDatabase{
            return Room.databaseBuilder(
                applicationContext,
                RecipeDatabase::class.java,
                "recipe-db"
            ).fallbackToDestructiveMigration().build();
        }
    }
}