package com.example.labimagecompressor.activities

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.labimagecompressor.databinding.ActivityMainBinding
import com.example.labimagecompressor.viewmodels.AbstractMainViewModel
import com.example.labimagecompressor.viewmodels.CompressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val _viewModel: AbstractMainViewModel by viewModel()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                lifecycleScope.launch {
                    setImage(it)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeAppState()
        setupUI()
    }

    private fun setupUI() {
        binding.btnSelectImage.setOnClickListener {
            _viewModel.selectImage()
        }
        binding.btnCancelCompression.setOnClickListener {

        }
        binding.btnBorrarTodo.setOnClickListener { view ->
            binding.imvFirstImage.setImageDrawable(null)
            view.visibility = GONE
        }
    }

    private fun observeAppState() {
        lifecycleScope.launch {
            _viewModel.appState.collect { state ->
                when (state.compressionState) {
                    CompressState.IDLE -> {
                        binding.main.setBackgroundColor(Color.LTGRAY)
                    }

                    CompressState.SELECT_IMAGE -> {
                        binding.main.setBackgroundColor(Color.BLUE)
                        openImagePicker()
                    }

                    CompressState.COMPRESSING -> {

                    }

                    CompressState.FAILED -> {

                    }
                }
            }
        }
    }

    private fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }


    private suspend fun setImage(imageUri: Uri) {
        // Step 4: Fetch the bitmap on the IO dispatcher
        val bitmap: Bitmap = withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // Use ImageDecoder for API 28 and above
                val source = ImageDecoder.createSource(contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                // Fallback to the old method for older versions
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            }
        }
        // Step 5: Set the image on the main thread
        binding.imvFirstImage.setImageBitmap(bitmap) // Set the image to your ImageView on the main thread
        withContext(Dispatchers.Main.immediate) {
            binding.btnBorrarTodo.visibility = VISIBLE
        }
        _viewModel.idle()
    }


}