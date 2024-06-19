package com.herdialfachri.pangankita.ui.ml.offline

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.databinding.ActivityOfflineClassificationBinding
import com.herdialfachri.pangankita.ui.data.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications

class OfflineClassificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOfflineClassificationBinding

    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfflineClassificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
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

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun analyzeImage(uri: Uri) {
        val imageClassifierHelper = ImageClassifierHelper(
            threshold = 0.1f,
            maxResults = 3,
            modelName = "model_with_metadata_txt.tflite",
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    showToast(error)
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    val intent =
                        Intent(this@OfflineClassificationActivity, ResultActivity::class.java)
                    intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
                    intent.putExtra(
                        ResultActivity.EXTRA_RESULT_LABEL,
                        results?.get(0)?.categories?.map { it.label }?.get(0).toString()
                    )
                    intent.putExtra(
                        ResultActivity.EXTRA_RESULT_SCORE,
                        results?.get(0)?.categories?.map { it.score }?.get(0).toString()
                    )
                    startActivity(intent)
                }
            })
        imageClassifierHelper.classifyStaticImage(uri)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}