package com.herdialfachri.pangankita.ui.ml.offline

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.herdialfachri.pangankita.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)
        val label = intent.getStringExtra(EXTRA_RESULT_LABEL)
        val score = intent.getStringExtra(EXTRA_RESULT_SCORE)

        if (imageUri != null) {
            val imageUriParse = Uri.parse(imageUri.toString())
            displayImage(imageUriParse)
            showResults(label.toString(), score.toString())
        } else {
            Log.e(ContentValues.TAG, "No image provided")
            finish()
        }
    }

    private fun showResults(label: String, score: String) {

        fun String.formatToString(): String {
            return "%.2f%%".format((this).toFloat() * 100)
        }
        binding.resultText.text = "$label ${score.formatToString()}"
    }

    private fun displayImage(uri: Uri) {
        Log.d(ContentValues.TAG, "Displaying image: $uri")
        binding.resultImage.setImageURI(uri)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT_LABEL = "extra_result_label"
        const val EXTRA_RESULT_SCORE = "extra_result_score"
    }
}
