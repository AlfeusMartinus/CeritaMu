package com.alfeus.storyapp.view.uploadStory

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alfeus.storyapp.databinding.ActivityUploadBinding
import com.alfeus.storyapp.view.getImageUri
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel: UploadViewModel by viewModels()
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permisi Diberikan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permisi Ditolak", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        viewModel.currentImageUri?.let {
            currentImageUri = it
            showImage()
        }

        observeViewModel()

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }

    }

    private fun observeViewModel() {
        viewModel.uploadStatus.observe(this) { result ->
            result?.let {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.btnUpload.isEnabled = false
                    }
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnUpload.isEnabled = true
                        Toast.makeText(this, "Berhasil Unggah Nih!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        binding.btnUpload.isEnabled = true
                        Toast.makeText(this, it.message ?: "Yah Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun uploadImage() {
        val description = binding.etDescription.text.toString().trim()

        if (currentImageUri == null) {
            Toast.makeText(this, "Pilih dulu dong gambar nya", Toast.LENGTH_SHORT).show()
            return
        }

        if (description.isEmpty()) {
            Toast.makeText(this, "Ayo tambahin deskripsi nya!", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(currentImageUri!!, this)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
        val descriptionBody = description.toRequestBody("text/plain".toMediaType())

        viewModel.uploadStory(imageMultipart, descriptionBody)
    }


    fun uriToFile(selectedUri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)
        val inputStream = contentResolver.openInputStream(selectedUri)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return tempFile
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        viewModel.currentImageUri = currentImageUri
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            viewModel.currentImageUri = currentImageUri
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            viewModel.currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}