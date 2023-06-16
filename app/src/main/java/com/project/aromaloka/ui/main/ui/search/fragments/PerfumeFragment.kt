package com.project.aromaloka.ui.main.ui.search.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.adapter.BrandAdapter
import com.project.aromaloka.adapter.NotesAdapter
import com.project.aromaloka.adapter.SearchedPerfumeAdapter
import com.project.aromaloka.databinding.FragmentPerfumeBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.ResponseSession
import com.project.aromaloka.ui.auth.LoginActivity
import com.project.aromaloka.ui.detail.BrandDetailActivity
import com.project.aromaloka.ui.detail.PerfumeDetailActivity
import com.project.aromaloka.ui.main.ui.home.HomeViewModel
import com.project.aromaloka.utils.Factory

class PerfumeFragment(private val queryParam: String, private val listQueryParam: Array<String>) : Fragment() {
    private var _binding: FragmentPerfumeBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: Factory
    private lateinit var token: String
    private val viewModel by viewModels<HomeViewModel> { factory }

    private lateinit var searchedPerfumeAdapter: SearchedPerfumeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerfumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = Factory.getInstance(requireContext())

        viewModel.getSession().observe(viewLifecycleOwner) { response ->
            if (response != null) {
                token = response.token
                viewModel.fetchPerfumeData(token)
            }
        }
        setupAdapters()
        setSearchRecyclerView()

        performSearch()
    }

    private fun setupAdapters() {
        searchedPerfumeAdapter = SearchedPerfumeAdapter { perfume ->
            val intent = Intent(requireContext(), PerfumeDetailActivity::class.java)
            intent.putExtra("perfume", perfume)
            startActivity(intent)
        }
    }

    private fun setSearchRecyclerView() {
        binding.apply {
            rvPerfumeSearch.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = searchedPerfumeAdapter
            }
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
        viewModel.perfumeList.observe(viewLifecycleOwner) { perfumeList ->
            val filterPerfume = perfumeList.filter { perfume: Perfume ->
                perfume.brand.lowercase().contains(lowercaseQuery)
                    || perfume.variant.lowercase().contains(lowercaseQuery)
                    || perfume.base_notes1.lowercase().contains(lowercaseQuery)
                    || perfume.base_notes2.lowercase().contains(lowercaseQuery)
                    || perfume.base_notes3.lowercase().contains(lowercaseQuery)
                    || perfume.top_notes1.lowercase().contains(lowercaseQuery)
                    || perfume.top_notes2.lowercase().contains(lowercaseQuery)
                    || perfume.top_notes3.lowercase().contains(lowercaseQuery)
                    || perfume.mid_notes1.lowercase().contains(lowercaseQuery)
                    || perfume.mid_notes2.lowercase().contains(lowercaseQuery)
                    || perfume.mid_notes3.lowercase().contains(lowercaseQuery)
            }
            searchedPerfumeAdapter.setData(filterPerfume as MutableList<Perfume>)
        }
    }

    private fun searchByListOfQuery() {
        viewModel.perfumeList.observe(viewLifecycleOwner) { perfumeList ->
            val searchedList = mutableListOf<Perfume>()
            listQueryParam.forEach { param ->
                val filterPerfume = perfumeList.filter { perfume: Perfume ->
                    perfume.brand.lowercase().contains(param.lowercase())
                        || perfume.variant.lowercase().contains(param.lowercase())
                        || perfume.base_notes1.lowercase().contains(param.lowercase())
                        || perfume.base_notes2.lowercase().contains(param.lowercase())
                        || perfume.base_notes3.lowercase().contains(param.lowercase())
                        || perfume.top_notes1.lowercase().contains(param.lowercase())
                        || perfume.top_notes2.lowercase().contains(param.lowercase())
                        || perfume.top_notes3.lowercase().contains(param.lowercase())
                        || perfume.mid_notes1.lowercase().contains(param.lowercase())
                        || perfume.mid_notes2.lowercase().contains(param.lowercase())
                        || perfume.mid_notes3.lowercase().contains(param.lowercase())

                }
                searchedList.addAll(filterPerfume)
            }
            searchedPerfumeAdapter.setData(searchedList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
