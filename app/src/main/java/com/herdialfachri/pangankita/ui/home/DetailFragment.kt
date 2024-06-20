package com.herdialfachri.pangankita.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.herdialfachri.pangankita.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil data dari bundle
        val name = arguments?.getString(ARG_NAME)
        val image = arguments?.getString(ARG_IMAGE)
        val whatsapp = arguments?.getString(ARG_WA)
        val description = arguments?.getString(ARG_DESC)
        val category = arguments?.getString(ARG_CAT)

        // Tampilkan data pada UI menggunakan view binding
        binding.tvDetailName.text = name
        binding.tvDetailSpecies.text = description
        binding.tvCategory.text = category

        // Ubah teks tombol WhatsApp menjadi "Buy" jika data WhatsApp tersedia
        if (!whatsapp.isNullOrEmpty()) {
            binding.btnWA.text = "Buy Now"
        }

        // Load gambar menggunakan Glide
        image?.let {
            Glide.with(this)
                .load(it)
                .into(binding.ivDetailImage)
        }

        // Set OnClickListener for the WhatsApp button
        binding.btnWA.setOnClickListener {
            whatsapp?.let {
                val intent = Intent(Intent.ACTION_VIEW)
                val url = "https://api.whatsapp.com/send?phone=$it"
                intent.data = Uri.parse(url)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_NAME = "arg_name"
        const val ARG_IMAGE = "arg_image"
        const val ARG_WA = "arg_wa"
        const val ARG_DESC = "arg_desc"
        const val ARG_CAT = "arg_cat"

        fun newInstance(
            name: String?,
            image: String?,
            whatsapp: String?,
            description: String?,
            category: String?
        ): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_IMAGE, image)
            args.putString(ARG_WA, whatsapp)
            args.putString(ARG_DESC, description)
            args.putString(ARG_CAT, category)
            fragment.arguments = args
            return fragment
        }
    }
}
