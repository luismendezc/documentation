package com.example.labimagecompressor.activities

import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.labimagecompressor.databinding.ActivityMainBinding
import com.example.labimagecompressor.viewmodels.AbstractMainViewModel
import com.example.labimagecompressor.viewmodels.AppState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val _viewModel: AbstractMainViewModel by viewModel()
    //private val imageCompressor: ImageCompressor by inject()


    private val PERMISSIONS_REQUEST_CODE = 1001

    private val requiredPermissions = mutableListOf<String>().apply {
        if (Build.VERSION.SDK_INT >= 33) { // Android 13+
            add("android.permission.READ_MEDIA_IMAGES") // Use direct string reference for Android 13+
        } else {
            add("android.permission.READ_EXTERNAL_STORAGE") // Manifest constant for Android 12 and below
        }
    }.toTypedArray()





    // ActivityResultLauncher for picking an image
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
            if (uri != null) {
                setMainimageAndStartCompression(uri)
            } else {
                _viewModel.idle()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeAppState()
        initialSetupUI()

        askForPermissionsIfNecessary()
    }

    private fun askForPermissionsIfNecessary() {
        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (!allGranted) {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // UI First setup
    private fun initialSetupUI() {
        binding.btnSelectImage.setOnClickListener {
            _viewModel.selectImage()
        }
        binding.btnCancelCompression.setOnClickListener {
            _viewModel.cancelCompression()
        }
        binding.btnBorrarTodo.setOnClickListener { view ->
            restartUI()
        }
    }

    private fun restartUI() {
        binding.imvFirstImage.setImageDrawable(null)
        binding.imvCompressedImage.setImageDrawable(null)

        binding.btnSelectImage.visibility = VISIBLE
        binding.btnCancelCompression.visibility = GONE
        binding.btnBorrarTodo.visibility = GONE
        _viewModel.restartToIdle()
    }

    private fun observeAppState() {
        lifecycleScope.launch {
            _viewModel.appState.collect { state ->
                when (state) {
                    AppState.IDLE -> {
                        binding.main.setBackgroundColor(Color.LTGRAY)
                    }

                    AppState.SELECT_IMAGE -> {
                        binding.main.setBackgroundColor(Color.BLUE)
                        openImagePicker()
                    }

                    AppState.COMPRESSING -> {
                        binding.main.setBackgroundColor(Color.GREEN)
                        binding.btnSelectImage.visibility = GONE
                        binding.btnCancelCompression.visibility = VISIBLE
                    }

                    is AppState.FINISHED -> {
                        binding.btnCancelCompression.visibility = GONE
                        state.imageUri?.let {
                            binding.imvCompressedImage.setImageURI(it)
                            binding.main.setBackgroundColor(Color.MAGENTA)
                        } ?: {
                            binding.main.setBackgroundColor(Color.CYAN)
                        }

                        binding.btnBorrarTodo.visibility = VISIBLE
                    }

                    AppState.CANCELLED -> {
                        binding.main.setBackgroundColor(Color.YELLOW)
                        restartUI()
                    }

                    AppState.FAILED -> {
                        binding.main.setBackgroundColor(Color.RED)
                    }
                }
            }
        }
    }

    // Contract for opening the image picker
    private fun openImagePicker() {
        pickImageLauncher.launch(
            PickVisualMediaRequest(
                ActivityResultContracts.PickVisualMedia.ImageOnly
            )
        )
    }

    private fun setMainimageAndStartCompression(imageUri: Uri) {
        binding.imvFirstImage.setImageURI(imageUri)
        _viewModel.compressImageProcess(imageUri, this)
    }

    /*
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
        val perro = imageCompressor.compressImage(imageUri, 10L)
        perro.let {
            val bit = BitmapFactory.decodeByteArray(it, 0, it!!.size)
            binding.imvCompressedImage.setImageBitmap(bit)
        }

        // Step 5: Set the image on the main thread
        binding.imvFirstImage.setImageBitmap(bitmap) // Set the image to your ImageView on the main thread
        withContext(Dispatchers.Main.immediate) {
            binding.btnBorrarTodo.visibility = VISIBLE
        }
        _viewModel.idle()
    }
    */


}