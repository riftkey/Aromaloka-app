package com.project.aromaloka.ui.main.ui.search.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.adapter.BrandAdapter
import com.project.aromaloka.databinding.FragmentBrandBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.ui.detail.BrandDetailActivity
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchViewModel
import com.project.aromaloka.utils.Factory

class BrandFragment(
    private val queryParam: String,
    private val listQueryParam: Array<String>
) : Fragment() {

    private var _binding: FragmentBrandBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: Factory

    private lateinit var brandAdapter: BrandAdapter
    private val viewModel by viewModels<SearchViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = Factory.getInstance(requireContext())

        viewModel.fetchBrands()
        setupAdapter()
        setSearchRecyclerView()
        performSearch()
    }

    private fun setupAdapter() {
        brandAdapter = BrandAdapter(){ brand ->
            val iBrand = Intent(this.requireContext(), BrandDetailActivity::class.java)
            val brandName = brand
            iBrand.putExtra("EXTRA_BRAND_NAME", brandName)
            startActivity(iBrand)
        }
    }

    private fun setSearchRecyclerView() {
        binding.rvBrandSearch.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = brandAdapter
        }
    }

    private fun performSearch() {
        when {
            queryParam.isNotEmpty() -> {
                searchByQuery()
            }
            listQueryParam.isNotEmpty() -> {
                searchByListOfQuery()
            }
            else -> Unit
        }
    }

    private fun searchByQuery() {
        val lowercaseQuery = queryParam.lowercase()
        viewModel.brandList.observe(viewLifecycleOwner) { brandList ->
            val filterBrand = brandList.filter { brand: Brand -> brand.name.lowercase().contains(lowercaseQuery) }
            brandAdapter.setData(filterBrand as MutableList<Brand>)
        }
    }

    private fun searchByListOfQuery() {
        viewModel.brandList.observe(viewLifecycleOwner) { brandList ->
            val searchedList = mutableListOf<Brand>()
            listQueryParam.forEach { param ->
                val filterBrand = brandList.filter { brand: Brand ->
                    brand.name.lowercase().contains(param.lowercase())
                }
                if (filterBrand.isNotEmpty()) searchedList.addAll(filterBrand)
            }
            brandAdapter.setData(searchedList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
