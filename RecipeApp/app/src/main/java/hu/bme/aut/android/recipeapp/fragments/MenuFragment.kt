package hu.bme.aut.android.recipeapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.recipeapp.MainActivity
import hu.bme.aut.android.recipeapp.R
import hu.bme.aut.android.recipeapp.adapter.RecipeAdapter
import hu.bme.aut.android.recipeapp.data.Category
import hu.bme.aut.android.recipeapp.data.RecipeDatabase
import hu.bme.aut.android.recipeapp.databinding.FragmentMenuBinding


class MenuFragment : Fragment() {
    private lateinit var binding : FragmentMenuBinding

    private lateinit var listener: MenuFragmentListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.soupButton.setOnClickListener {
            listener.onCategorySelected(Category.SOUP)
            findNavController().navigate(R.id.action_menuFragment_to_recipeListFragment)
        }
        binding.mainCourseButton.setOnClickListener{
            listener.onCategorySelected(Category.MAINCOURSE)
            findNavController().navigate(R.id.action_menuFragment_to_recipeListFragment)
        }
        binding.dessertButton.setOnClickListener{
            listener.onCategorySelected(Category.DESSERT)
            findNavController().navigate(R.id.action_menuFragment_to_recipeListFragment)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? MenuFragmentListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")
    }


    interface MenuFragmentListener{
        fun onCategorySelected(category: Category)
    }
}
