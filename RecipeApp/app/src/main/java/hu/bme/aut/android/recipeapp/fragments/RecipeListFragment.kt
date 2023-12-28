package hu.bme.aut.android.recipeapp.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.recipeapp.MainActivity
import hu.bme.aut.android.recipeapp.R
import hu.bme.aut.android.recipeapp.adapter.RecipeAdapter
import hu.bme.aut.android.recipeapp.data.Category
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.data.RecipeDatabase
import hu.bme.aut.android.recipeapp.databinding.FragmentRecipeListBinding
import kotlin.concurrent.thread


class RecipeListFragment : Fragment(), RecipeAdapter.RecipeClickedListener{

    private lateinit var binding:FragmentRecipeListBinding

    private lateinit var listener: RecipeListFragmentListener

    private lateinit var adapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecipeListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        binding.fab.setOnClickListener{
            findNavController().navigate(R.id.action_recipeListFragment_to_newRecipeFragment)
        }

    }

    private fun initRecycleView(){
        adapter = listener.onList()
        adapter.setOnRecipeClickedListener(this)
        binding.rvList.layoutManager = LinearLayoutManager(context)
        binding.rvList.adapter = adapter
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? RecipeListFragmentListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }

    interface RecipeListFragmentListener {
        fun onList(): RecipeAdapter

        fun onShowDetails(recipe: Recipe)
    }

    override fun onClicked(recipe: Recipe) {
        listener.onShowDetails(recipe)
        findNavController().navigate(R.id.action_recipeListFragment_to_recipeDetailsFragment)
    }



}