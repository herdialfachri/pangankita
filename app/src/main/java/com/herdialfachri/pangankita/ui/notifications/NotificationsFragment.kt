package com.herdialfachri.pangankita.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.herdialfachri.pangankita.MainActivity
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels()

    private var bottomNav: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton = view.findViewById<Button>(R.id.btn_logout)
        val loadingKeluar = view.findViewById<ProgressBar>(R.id.loadingKeluar)

        val sharedPreferences =
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        // Set initial visibility based on login state
        if (isLoggedIn) {
            binding.btnMasuk.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
            loadUserData()
        } else {
            binding.btnMasuk.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
            binding.btnGotoprofile.visibility = View.GONE
            binding.ivGotoprofile.visibility = View.GONE
            binding.tvGotoprofile.visibility = View.GONE
        }

        logoutButton.setOnClickListener {
            loadingKeluar.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.logout()
                clearUserData() // Hapus data pengguna setelah logout
            }, 1300) // 1.3 detik
        }

        viewModel.logoutEvent.observe(viewLifecycleOwner, Observer {
            val intent = Intent(activity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        })

        // Mendapatkan referensi ke bottom navigation view dari aktivitas
        bottomNav = activity?.findViewById(R.id.nav_view)

        // Mengatur listener untuk navigasi tombol-tombol
        setupNavigationListeners()
    }

    override fun onResume() {
        super.onResume()
        // Munculkan BottomNavigationView ketika kembali ke fragment ini
        bottomNav?.visibility = View.VISIBLE
    }

    private fun setupNavigationListeners() {
        // Fungsi berpindah dari setiap button ke fragment lain
        binding.btnGotoprofile.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_profileFragment)
        )
        binding.btnMasuk.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_loginActivity)
        )
        binding.btnTentangAplikasi.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_aboutFragment)
        )
        binding.btnHubungiDesa.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_contactFragment)
        )
        binding.buttonUndang.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_navigation_notifications_to_shareFragment)
        )
        binding.buttonTentangAplikasi.setOnClickListener {
            // Menampilkan pesan "Fitur sedang dikembangkan"
            showToast("Fitur cara pakai aplikasi sedang dikembangkan")
        }
        binding.buttonHakCipta.setOnClickListener {
            // Menampilkan pesan "Fitur sedang dikembangkan"
            showToast("Fitur hak cipta sedang dikembangkan")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun loadUserData() {
        // Implementasikan fungsi ini untuk memuat data pengguna dari SharedPreferences ke tampilan
    }

    private fun clearUserData() {
        // Hapus data pengguna dari SharedPreferences
        val sharedPreferences =
            requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}

