package com.herdialfachri.pangankita.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.herdialfachri.pangankita.databinding.FragmentQuestionsBinding

class QuestionsFragment : Fragment() {

    private var _binding: FragmentQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarAbout.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Tambahkan logika untuk membuka WhatsApp ketika btnAsk diklik
        binding.btnAsk.setOnClickListener {
            openWhatsApp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openWhatsApp() {
        val phoneNumber = "+621234567890" // Ganti dengan nomor telepon yang diinginkan
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}
