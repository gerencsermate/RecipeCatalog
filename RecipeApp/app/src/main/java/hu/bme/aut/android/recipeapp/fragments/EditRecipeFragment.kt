package hu.bme.aut.android.recipeapp.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
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
import hu.bme.aut.android.recipeapp.data.Recipe
import hu.bme.aut.android.recipeapp.databinding.FragmentEditRecipeBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class EditRecipeFragment : Fragment() {
    private lateinit var binding: FragmentEditRecipeBinding

    private lateinit var listener: EditRecipeFragmentListener

    private lateinit var recipeToEdit: Recipe

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
        binding = FragmentEditRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeData()

        binding.btnEditPicture.setOnClickListener{
            addPicture()
            binding.btnDeleteEditPicture.isEnabled = true
            binding.btnEditPicture.isEnabled = false
        }

        binding.btnDeleteEditPicture.setOnClickListener{
            requireContext().contentResolver.delete(pictureUri!!, null, null)
            pictureUri = null
            binding.ivEditRecipePictureContainer.setImageURI(null)
            binding.btnEditPicture.isEnabled = true
            binding.btnDeleteEditPicture.isEnabled = false
        }

        binding.btnSubmitChanges.setOnClickListener{
            if(binding.etChangedName.text.isEmpty()){
                binding.etChangedName.error = "A mező kitöltése kötelező!"
            }
            else if(binding.etChangedPrepTime.text.isEmpty()){
                binding.etChangedPrepTime.error = "A mező kitöltése kötelező!"
            }
            else if(binding.etChangedDescription.text.isEmpty()){
                binding.etChangedPrepTime.error = "A mező kitöltése kötelező!"
            }
            else{
                updateRecipeData()
                listener.onRecipeChanged(recipeToEdit)
                findNavController().navigate(R.id.action_editRecipeFragment_to_recipeListFragment)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? EditRecipeFragmentListener
            ?: throw RuntimeException("Activity must implement the NewShoppingItemDialogListener interface!")

        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()){
                it -> if(it) {
            Log.i("savingPicture", "Picture saved succesfully!, path: $currentImagePath")
            binding.ivEditRecipePictureContainer.setImageURI(pictureUri)
        }else{
            Log.i("savingPicture", "An error occured, picture not saved!")
        }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {  }
    }

    interface EditRecipeFragmentListener{
        fun onRecipeChanged(recipe:Recipe)

        fun onLoad(): Recipe
    }

    private fun updateRecipeData(){
        recipeToEdit.name = binding.etChangedName.text.toString()
        recipeToEdit.prepTime = binding.etChangedPrepTime.text.toString().toInt()
        recipeToEdit.difficulty = binding.changedRating.rating.toInt()
        recipeToEdit.steps = binding.etChangedDescription.text.toString()
        recipeToEdit.picture = pictureUri
    }


    private fun initializeData(){
        recipeToEdit = listener.onLoad()
        binding.etChangedName.setText(recipeToEdit.name)
        binding.etChangedPrepTime.setText(recipeToEdit.prepTime.toString())
        binding.changedRating.rating = recipeToEdit.difficulty.toFloat()
        binding.etChangedDescription.setText(recipeToEdit.steps)
        if(recipeToEdit.picture != null){
            binding.ivEditRecipePictureContainer.setImageURI(recipeToEdit.picture)
            pictureUri = recipeToEdit.picture
            binding.btnDeleteEditPicture.isEnabled = true
            binding.btnEditPicture.isEnabled = false
        }
        else{
            binding.btnEditPicture.isEnabled = true
            binding.btnDeleteEditPicture.isEnabled = false
        }

    }

    private fun addPicture(){
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val storageFile = File.createTempFile("Recipe_$timeStamp", ".png", storageDir)
            .apply { currentImagePath = absolutePath }
        pictureUri = FileProvider.getUriForFile(requireContext(), "hu.bme.aut.android.recipeapp.provider", storageFile)

        takePicture.launch(pictureUri)
    }


}