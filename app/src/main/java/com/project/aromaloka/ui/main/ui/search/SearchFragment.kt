package com.project.aromaloka.ui.main.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.project.aromaloka.adapter.BrandAdapter
import com.project.aromaloka.adapter.NotesAdapter
import com.project.aromaloka.databinding.FragmentSearchBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.models.Note
import com.project.aromaloka.ui.main.ui.search.filter.FilterActivity
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchResultActivity
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchViewModel
import com.project.aromaloka.utils.Factory

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private lateinit var factory: Factory
    private val binding get() = _binding!!
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var notesAdapter: NotesAdapter
    private val viewModel by viewModels<SearchViewModel> { factory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = Factory.getInstance(requireContext())

        viewModel.fetchBrands()
        viewModel.fetchNotes()
        setListeners()
        setupAdapters()
        setSearchRecyclerView()
        observeSearchData()
    }

    private fun setListeners() {
        binding.apply {
            svPerfume.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = textView.text.toString().trim()
                    performBrandSearch(query)
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

            btnFilter.setOnClickListener {
                val iFilter = Intent(requireContext(), FilterActivity::class.java)
                startActivity(iFilter)
            }
        }
    }


    private fun performBrandSearch(query: String) {
        val iSearchResult = Intent(requireContext(), SearchResultActivity::class.java)
        iSearchResult.putExtra("query", query)
        startActivity(iSearchResult)
    }


    private fun performNoteSearch(note: Note) {
        val iSearchResult = Intent(requireContext(), SearchResultActivity::class.java)
        iSearchResult.putExtra("query", note.name) // Use note name as the search query
        startActivity(iSearchResult)
    }

    private fun setupAdapters() {
        brandAdapter = BrandAdapter() { brand ->
            performBrandSearch(brand.name)
        }

        notesAdapter = NotesAdapter(){notes ->
            performNoteSearch(notes)

        }
    }

    private fun setSearchRecyclerView() {
        binding.apply {
            rvNotes.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = notesAdapter
            }

            rvBrand.apply {
                layoutManager = GridLayoutManager(requireContext(), 2)
                adapter = brandAdapter
            }
        }
    }

    private fun observeSearchData() {
        viewModel.brandList.observe(viewLifecycleOwner) { brandList ->
            brandAdapter.setData(brandList as MutableList<Brand>)
        }

        viewModel.noteList.observe(viewLifecycleOwner) { noteList ->
            notesAdapter.setData(noteList as kotlin.collections.MutableList<Note>)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        val titleArray = arrayOf(
            "Perfume",
            "Brand"
        )
    }
}