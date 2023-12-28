package hu.bme.aut.android.recipeapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.databinding.ItemRecipeBinding



class RecipeAdapter() : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>(){
    private val recipes = mutableListOf<Recipe>()

    private lateinit var clickListener: RecipeClickedListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= RecipeViewHolder(
        ItemRecipeBinding.inflate(LayoutInflater.from(parent.context, ), parent, false)
    )

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.binding.tvName.text = recipe.name
        holder.binding.tvDifficulty.rating = recipe.difficulty.toFloat()
        holder.binding.tvPrepTime.text = "${recipe.prepTime} perc"

        holder.binding.btnDetails.setOnClickListener{
            clickListener.onClicked(recipe)
        }
    }

    fun addRecipe(recipe: Recipe){
        recipes.add(recipe)
        notifyItemInserted(recipes.size - 1)
    }

    fun updateRecipes(newRecipes: List<Recipe>){
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    fun deleteRecipe(recipe: Recipe){
        var index = recipes.indexOf(recipe)
        recipes.remove(recipe)
        notifyItemRemoved(index)
    }

    fun updateRecipeData(recipe: Recipe){
        recipes.removeIf { it.id == recipe.id }
        recipes.add(recipe)
        notifyDataSetChanged()
    }

    interface RecipeClickedListener{
        fun onClicked(recipe: Recipe)
    }

    fun setOnRecipeClickedListener(listener: RecipeClickedListener){
        clickListener = listener
    }


    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)
}