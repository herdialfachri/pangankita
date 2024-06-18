package com.herdialfachri.pangankita.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ml.ModelUnquant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class DashboardFragment : Fragment() {

    private lateinit var result: TextView
//    private lateinit var confidence: TextView
    private lateinit var ingredients: TextView
    private lateinit var imageView: ImageView
    private lateinit var picture: Button
    private lateinit var galleryButton: Button
    private lateinit var description: TextView
    private val imageSize = 224

    // Menambahkan map untuk deskripsi makanan
    private val foodDescriptions = mapOf(
        "Ayam Betutu" to R.string.ayam_betutu,
        "Beberuk Terong" to R.string.beberuk_terong,
        "Coto Makasar" to R.string.coto_makassar,
        "Gudeg" to R.string.gudeg,
        "Kerak Telor" to R.string.kerak_telor,
        "Mie Aceh" to R.string.mie_aceh,
        "Nasi Kuning" to R.string.nasi_kuning,
        "Nasi Pecel" to R.string.nasi_pecel,
        "Papeda" to R.string.papeda,
        "Pempek" to R.string.pempek,
        "Peyeum" to R.string.peuyeum,
        "Rawon" to R.string.rawon,
        "Rendang" to R.string.rendang,
        "Sate Madura" to R.string.sate_madura,
        "Serabi" to R.string.serabi,
        "Soto Banjar" to R.string.soto_banjar,
        "Soto Lamongan" to R.string.soto_lamongan,
        "Tahu Sumedang" to R.string.tahu_sumedang,
        "Bakso" to R.string.bakso,
        "Bebek Betutu" to R.string.bebek_betutu,
        "Gado-Gado" to R.string.gado_gado,
        "Nasi Goreng" to R.string.nasi_goreng,
        "Batagor" to R.string.batagor,
        "Ayam Goreng" to R.string.ayam_goreng,
        "Ayam Pop" to R.string.ayam_pop,
        "Dendeng Batokok" to R.string.dendeng_batokok,
        "Gulai Ikan" to R.string.gulai_ikan,
        "Gulai Tambusu" to R.string.gulai_tambusu,
        "Gulai Tunjang" to R.string.gulai_tunjang,
        "Telur Balado" to R.string.telur_balado,
        "Telur Dadar" to R.string.telur_dadar
    )

    private val foodIngredients = mapOf(
        "Ayam Betutu" to R.string.bahan_ayam_betutu,
        "Beberuk Terong" to R.string.bahan_beberuk_terong,
        "Coto Makasar" to R.string.bahan_coto_makassar,
        "Gudeg" to R.string.bahan_gudeg,
        "Kerak Telor" to R.string.bahan_kerak_telor,
        "Mie Aceh" to R.string.bahan_mie_aceh,
        "Nasi Kuning" to R.string.bahan_nasi_kuning,
        "Nasi Pecel" to R.string.bahan_nasi_pecel,
        "Papeda" to R.string.bahan_papeda,
        "Pempek" to R.string.bahan_pempek,
        "Peyeum" to R.string.bahan_peuyeum,
        "Rawon" to R.string.bahan_rawon,
        "Rendang" to R.string.bahan_rendang,
        "Sate Madura" to R.string.bahan_sate_madura,
        "Serabi" to R.string.bahan_serabi,
        "Soto Banjar" to R.string.bahan_soto_banjar,
        "Soto Lamongan" to R.string.bahan_soto_lamongan,
        "Tahu Sumedang" to R.string.bahan_tahu_sumedang,
        "Bakso" to R.string.bahan_bakso,
        "Bebek Betutu" to R.string.bahan_bebek_betutu,
        "Gado-Gado" to R.string.bahan_gado_gado,
        "Nasi Goreng" to R.string.bahan_nasi_goreng,
        "Batagor" to R.string.bahan_batagor,
        "Ayam Goreng" to R.string.bahan_ayam_goreng,
        "Ayam Pop" to R.string.bahan_ayam_pop,
        "Dendeng Batokok" to R.string.bahan_dendeng_batokok,
        "Gulai Ikan" to R.string.bahan_gulai_ikan,
        "Gulai Tambusu" to R.string.bahan_gulai_tambusu,
        "Gulai Tunjang" to R.string.bahan_gulai_tunjang,
        "Telur Balado" to R.string.bahan_telur_balado,
        "Telur Dadar" to R.string.bahan_telur_dadar
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        result = view.findViewById(R.id.result)
//        confidence = view.findViewById(R.id.confidence)
        imageView = view.findViewById(R.id.imageView)
        picture = view.findViewById(R.id.button)
        galleryButton = view.findViewById(R.id.galleryButton)
        description = view.findViewById(R.id.description)
        ingredients = view.findViewById(R.id.ingred)

        picture.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, 1)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
            }
        }

        galleryButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            } else {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
        }

        return view
    }

    private fun classifyImage(image: Bitmap) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val model = ModelUnquant.newInstance(requireContext())

                val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
                val byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
                byteBuffer.order(ByteOrder.nativeOrder())

                val intValues = IntArray(imageSize * imageSize)
                image.getPixels(intValues, 0, image.width, 0, 0, image.width, image.height)

                var pixel = 0
                for (i in 0 until imageSize) {
                    for (j in 0 until imageSize) {
                        val `val` = intValues[pixel++] // RGB
                        byteBuffer.putFloat(((`val` shr 16) and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat(((`val` shr 8) and 0xFF) * (1f / 255f))
                        byteBuffer.putFloat((`val` and 0xFF) * (1f / 255f))
                    }
                }

                inputFeature0.loadBuffer(byteBuffer)

                val outputs = model.process(inputFeature0)
                val outputFeature0 = outputs.outputFeature0AsTensorBuffer

                val confidences = outputFeature0.floatArray
                var maxPos = 0
                var maxConfidence = 0f
                for (i in confidences.indices) {
                    if (confidences[i] > maxConfidence) {
                        maxConfidence = confidences[i]
                        maxPos = i
                    }
                }

                val classes = arrayOf("Ayam Betutu", "Beberuk Terong", "Coto Makasar", "Gudeg",
                    "Kerak Telor", "Mie Aceh", "Nasi Kuning", "Nasi Pecel", "Papeda", "Pempek",
                    "Peyeum", "Rawon", "Rendang", "Sate Madura", "Serabi", "Soto Banjar", "Soto Lamongan",
                    "Tahu Sumedang", "Bakso", "Bebek Betutu", "Gado-Gado", "Nasi Goreng", "Batagor",
                    "Ayam Goreng", "Ayam Pop", "Dendeng Batokok", "Gulai Ikan", "Gulai Tambusu",
                    "Gulai Tunjang", "Telur Balado", "Telur Dadar")

                val classificationResult = classes[maxPos]

                var s = ""
                for (i in classes.indices) {
                    s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100)
                }

                val descriptionResId = foodDescriptions[classificationResult] ?: R.string.description_not_available
                val ingredientsResId = foodIngredients[classificationResult] ?: R.string.ingredients_not_available

                withContext(Dispatchers.Main) {
                    result.text = classificationResult
                    description.setText(descriptionResId)
                    ingredients.setText(ingredientsResId)
                    model.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val image = data?.extras?.get("data") as Bitmap
            val dimension = Math.min(image.width, image.height)
            val thumbnail = ThumbnailUtils.extractThumbnail(image, dimension, dimension)
            imageView.setImageBitmap(thumbnail)

            classifyImage(Bitmap.createScaledBitmap(thumbnail, imageSize, imageSize, false))
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            val imageStream = requireContext().contentResolver.openInputStream(imageUri!!)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            imageView.setImageBitmap(selectedImage)

            classifyImage(Bitmap.createScaledBitmap(selectedImage, imageSize, imageSize, false))
        }
    }
}
