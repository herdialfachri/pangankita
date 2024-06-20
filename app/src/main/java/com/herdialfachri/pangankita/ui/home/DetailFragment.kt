package com.herdialfachri.pangankita.ui.home

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
        val status = arguments?.getString(ARG_STATUS)

        // Tampilkan data pada UI menggunakan view binding
        binding.tvDetailName.text = name
        binding.tvDetailStatus.text = status

        // Load gambar menggunakan Glide
        image?.let {
            Glide.with(this)
                .load(it)
                .into(binding.ivDetailImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_NAME = "arg_name"
        const val ARG_IMAGE = "arg_image"
        const val ARG_STATUS = "arg_status"
        const val ARG_SPECIES = "arg_species"

        fun newInstance(
            name: String?,
            image: String?,
            status: String?,
            species: String?
        ): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putString(ARG_NAME, name)
            args.putString(ARG_IMAGE, image)
            args.putString(ARG_STATUS, status)
            args.putString(ARG_SPECIES, species)
            fragment.arguments = args
            return fragment
        }
    }
}