package com.herdialfachri.pangankita.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.herdialfachri.pangankita.R
import com.herdialfachri.pangankita.ui.data.home_api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var mortyAdapter: MortyAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private var allProducts: List<DataItem> = emptyList()

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
        val categories = view.findViewById<RecyclerView>(R.id.rvKategori)
        val searchView = view.findViewById<SearchView>(R.id.search)

        // Setup category RecyclerView
        categories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categories.setHasFixedSize(true)

        // Fetch categories and set adapter
        HomeConfig.getService().getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                if (response.isSuccessful) {
                    val categoryData = response.body()?.data ?: emptyList()
                    categoryAdapter = CategoryAdapter(categoryData, this@HomeFragment)
                    categories.adapter = categoryAdapter
                }
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        // Setup product RecyclerView
        HomeConfig.getService().getMorty().enqueue(object : Callback<SayuranResponse> {
            override fun onResponse(call: Call<SayuranResponse>, response: Response<SayuranResponse>) {
                if (response.isSuccessful) {
                    val responseMorty = response.body()
                    allProducts = (responseMorty?.data ?: emptyList()) as List<DataItem>
                    mortyAdapter = MortyAdapter(allProducts)
                    morty.apply {
                        layoutManager = GridLayoutManager(context, 2)
                        setHasFixedSize(true)
                        adapter = mortyAdapter
                    }
                }
            }

            override fun onFailure(call: Call<SayuranResponse>, t: Throwable) {
                Toast.makeText(context, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mortyAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onCategoryClick(category: CategoryItem) {
        val filteredProducts = allProducts.filter { it.category == category.name }
        mortyAdapter = MortyAdapter(filteredProducts)
        view?.findViewById<RecyclerView>(R.id.rvProduk)?.adapter = mortyAdapter
    }
}
