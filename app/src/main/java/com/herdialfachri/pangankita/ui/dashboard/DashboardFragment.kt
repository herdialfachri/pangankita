package com.herdialfachri.pangankita.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.herdialfachri.pangankita.databinding.FragmentDashboardBinding
import com.herdialfachri.pangankita.ui.ml.offline.OfflineClassificationActivity
import com.herdialfachri.pangankita.ui.ml.online.OnlineClasifyActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toOnline.setOnClickListener {
            val intent = Intent(requireActivity(), OnlineClasifyActivity::class.java)
            startActivity(intent)
        }
        binding.toOffline.setOnClickListener {
            val intent = Intent(requireActivity(), OfflineClassificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
