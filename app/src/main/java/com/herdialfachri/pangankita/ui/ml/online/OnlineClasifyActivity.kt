package com.herdialfachri.pangankita.ui.ml.online

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.gson.Gson
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.databinding.ActivityOnlineClassifyBinding
import com.herdialfachri.pangankita.ui.data.api.ApiConfig
import com.herdialfachri.pangankita.ui.data.api.FileUploadResponse
import com.herdialfachri.pangankita.ui.ml.online.CameraActivity.Companion.CAMERAX_RESULT
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException

class OnlineClasifyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnlineClassifyBinding
    private lateinit var progressIndicator: LinearProgressIndicator
    private lateinit var predictionTextView: TextView

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnlineClassifyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressIndicator = findViewById(R.id.progressIndicator)
        predictionTextView = findViewById(R.id.resultTextView)

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, CAMERAX_RESULT)
    }

    private fun uploadImage() {
        val imageUri = currentImageUri ?: run {
            Toast.makeText(this, "Ambil atau masukkan gambar terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
            return
        }
        val file = uriToFile(imageUri, this)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestImageFile
        )
        lifecycleScope.launch {
            try {
                showLoading(true)
                val service = ApiConfig.getApiService().uploadImage(imageMultipart)
                val responseBody = Gson().toJson(service)
                val response = Gson().fromJson(responseBody, FileUploadResponse::class.java)
                showLoading(false)
                predictionTextView.text = response.data?.prediction ?: "No prediction"
            } catch (e: HttpException) {
                showLoading(false)
                Log.e("MainActivity", "Failed to upload image: ${e.message()}")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showImage() {
        binding.previewImageView.setImageURI(currentImageUri)
    }

    companion object {
        private val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}