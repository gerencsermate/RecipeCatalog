package hu.bme.aut.android.recipeapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.recipeapp.R
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.databinding.FragmentRecipeDetailsBinding

class RecipeDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRecipeDetailsBinding
    private lateinit var listener: RecipeDetailsFragmentListener
    private lateinit var recipeToShow : Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipeDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDetails()
        binding.toolbar.inflateMenu(R.menu.menu)



        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.editRecipe ->{
                    findNavController().navigate(R.id.action_recipeDetailsFragment_to_editRecipeFragment)
                    true
                }
                R.id.deleteRecipe->{
                    if(recipeToShow.picture != null){
                        requireContext().contentResolver.delete(recipeToShow.picture!!, null, null)
                    }
                    listener.onDeleteRecipe(recipeToShow)
                    findNavController().navigate(R.id.action_recipeDetailsFragment_to_recipeListFragment)
                    true
                }
                R.id.shareRecipe -> {
                    sendRecipe()
                    true
                }
                else->false
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as? RecipeDetailsFragmentListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    interface RecipeDetailsFragmentListener{
        fun onShowingDetails() : Recipe

        fun onDeleteRecipe(recipe: Recipe)
    }


    private fun initDetails(){
        recipeToShow = listener.onShowingDetails()
        binding.tvDetailsName.text = recipeToShow.name
        binding.tvDetailsPrepTime.text = recipeToShow.prepTime.toString()
        binding.rbDetailsDifficulty.rating = recipeToShow.difficulty.toFloat()
        binding.tvDetailsSteps.text = recipeToShow.steps
        if(recipeToShow.picture != null){
            binding.ivRecipeDetailsPictureContainer.setImageURI(recipeToShow.picture)
        }
    }

    private fun sendRecipe(){
        val intent = Intent(Intent.ACTION_SEND)

        var messageText = "${recipeToShow.name}\n" +
                            "Elkészítési idő: ${recipeToShow.prepTime} perc\n" +
                            "Nehézség: ${recipeToShow.difficulty}\n" +
                            "Leírás: ${recipeToShow.steps}"


        intent.putExtra(Intent.EXTRA_TEXT, messageText)

        intent.type ="message/rfc822"

        startActivity(Intent.createChooser(intent, "Válassza ki melyik alkalmazást szeretné használni!"))
    }
}