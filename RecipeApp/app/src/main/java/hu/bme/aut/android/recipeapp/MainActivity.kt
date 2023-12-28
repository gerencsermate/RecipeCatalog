package hu.bme.aut.android.recipeapp

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import hu.bme.aut.android.recipeapp.adapter.RecipeAdapter
import hu.bme.aut.android.recipeapp.data.Category
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.data.RecipeDatabase
import hu.bme.aut.android.recipeapp.databinding.ActivityMainBinding
import hu.bme.aut.android.recipeapp.fragments.EditRecipeFragment
import hu.bme.aut.android.recipeapp.fragments.MenuFragment
import hu.bme.aut.android.recipeapp.fragments.NewRecipeFragment
import hu.bme.aut.android.recipeapp.fragments.RecipeDetailsFragment
import hu.bme.aut.android.recipeapp.fragments.RecipeListFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), RecipeListFragment.RecipeListFragmentListener, EditRecipeFragment.EditRecipeFragmentListener, MenuFragment.MenuFragmentListener, RecipeDetailsFragment.RecipeDetailsFragmentListener, NewRecipeFragment.NewRecipeFragmentListener {
    private lateinit var selectedCategory: Category

    private lateinit var database: RecipeDatabase
    private lateinit var adapter: RecipeAdapter

    private lateinit var binding: ActivityMainBinding

    private lateinit var selectedRecipe : Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = RecipeDatabase.getDatabase(applicationContext)

        //adapter will be initialized in onList() method
        adapter = RecipeAdapter()

        binding = ActivityMainBinding.inflate(layoutInflater)

    }



    fun initAdapter(){
        thread {
            val items = database.recipeDao().getRecipesFromCategory(selectedCategory)
            runOnUiThread{
                adapter.updateRecipes(items)
            }
        }
    }

    override fun onNewRecipeAdded(recipe: Recipe) {
        thread{
            recipe.category = selectedCategory
            val insertId = database.recipeDao().insert(recipe)
            recipe.id = insertId
            runOnUiThread {
                adapter.addRecipe(recipe)
            }
        }
    }

    override fun onList(): RecipeAdapter {
        initAdapter()
        return adapter;
    }

    override fun onShowDetails(recipe: Recipe) {
        selectedRecipe = recipe
    }

    override fun onCategorySelected(category: Category) {
        selectedCategory = category
    }

    override fun onShowingDetails(): Recipe {
        return selectedRecipe
    }

    override fun onDeleteRecipe(recipe: Recipe) {
        thread{
            database.recipeDao().delete(recipe)
            runOnUiThread{
                adapter.deleteRecipe(recipe)
            }
        }
    }


    override fun onRecipeChanged(recipe: Recipe) {
        thread{
            database.recipeDao().update(recipe)
            runOnUiThread{
                adapter.updateRecipeData(recipe)
            }
        }
    }

    override fun onLoad(): Recipe {
        return selectedRecipe
    }
}