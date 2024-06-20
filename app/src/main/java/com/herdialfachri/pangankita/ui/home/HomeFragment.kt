package com.herdialfachri.pangankita.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.data.home_api.HomeConfig
import com.herdialfachri.pangankita.ui.data.home_api.MortyAdapter
import com.herdialfachri.pangankita.ui.data.home_api.SayuranResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout untuk fragment ini
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val morty = view.findViewById<RecyclerView>(R.id.rvProduk)

        HomeConfig.getService().getMorty().enqueue(object : Callback<SayuranResponse> {
            override fun onResponse(call: Call<SayuranResponse>, response: Response<SayuranResponse>) {
                if (response.isSuccessful) {
                    val responseMorty = response.body()
                    var dataMorty = responseMorty?.data
                    dataMorty = dataMorty?.take(20)
                    val mortyAdapter = MortyAdapter(dataMorty)
                    morty.apply {
                        layoutManager = GridLayoutManager(context, 2)
                        setHasFixedSize(true)
                        mortyAdapter.notifyDataSetChanged()
                        adapter = mortyAdapter
                    }
                }
            }

            override fun onFailure(call: Call<SayuranResponse>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
