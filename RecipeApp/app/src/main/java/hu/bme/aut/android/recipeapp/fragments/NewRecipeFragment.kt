package hu.bme.aut.android.recipeapp.fragments


import android.content.Context

import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import hu.bme.aut.android.recipeapp.R
import hu.bme.aut.android.recipeapp.data.Category
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.databinding.FragmentNewRecipeBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class NewRecipeFragment : Fragment() {
    private lateinit var binding: FragmentNewRecipeBinding

    private lateinit var listener: NewRecipeFragmentListener

    private lateinit var takePicture: ActivityResultLauncher<Uri>

    private lateinit var currentImagePath: String

    private var pictureUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewPicture.setOnClickListener{
            addPicture()
            binding.btnNewPicture.isEnabled = false
            binding.btnDeleteNewPicture.isEnabled = true
        }

        binding.btnDeleteNewPicture.setOnClickListener{
            requireContext().contentResolver.delete(pictureUri!!,null, null)
            pictureUri = null
            binding.ivNewRecipePictureContainer.setImageURI(null)
            binding.btnNewPicture.isEnabled = false
            binding.btnDeleteNewPicture.isEnabled = true
        }

        binding.btmSubmitRecipe.setOnClickListener{
            if(binding.etNewName.text.isEmpty()){
                binding.etNewName.error = "A mező kitöltése kötelező!"
            }
            else if(binding.etNewPrepTime.text.isEmpty()){
                binding.etNewPrepTime.error = "A mező kitöltése kötelező!"
            }
            else if(binding.etNewDescription.text.isEmpty()){
                binding.etNewDescription.error = "A mező kitöltése kötelező!"
            }
            else{
                listener.onNewRecipeAdded(getNewRecipe())
                findNavController().navigate(R.id.action_newRecipeFragment_to_recipeListFragment)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? NewRecipeFragmentListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()){
            it -> if(it) {
                        Log.i("savingPicture", "Picture saved succesfully!, path: $currentImagePath")
                        binding.ivNewRecipePictureContainer.setImageURI(pictureUri)
                    }else{
                        Log.i("savingPicture", "An error occured, picture not saved!")
                    }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {  }
    }



    interface NewRecipeFragmentListener{
        fun onNewRecipeAdded(recipe:Recipe)
    }

    private fun getNewRecipe(): Recipe{
        return Recipe(name = binding.etNewName.text.toString(),
            prepTime = binding.etNewPrepTime.text.toString().toInt(),
            difficulty = binding.newRating.rating.toInt(),
            steps = binding.etNewDescription.text.toString(),
            category = Category.SOUP,
            picture = pictureUri);
    }

    private fun addPicture(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(DIRECTORY_PICTURES)
        val storageFile = File.createTempFile("Recipe_$timeStamp", ".png", storageDir)
                        .apply { currentImagePath = absolutePath }
        pictureUri = FileProvider.getUriForFile(requireContext(), "hu.bme.aut.android.recipeapp.provider", storageFile)

        takePicture.launch(pictureUri)
    }

}