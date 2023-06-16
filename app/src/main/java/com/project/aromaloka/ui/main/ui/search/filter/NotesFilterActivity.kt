package com.project.aromaloka.ui.main.ui.search.filter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.project.aromaloka.adapter.NotesFilterAdapter
import com.project.aromaloka.databinding.ActivityNotesFilterBinding
import com.project.aromaloka.models.Note
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchResultActivity
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchViewModel
import com.project.aromaloka.utils.Factory

@RequiresApi(Build.VERSION_CODES.M)
class NotesFilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotesFilterBinding
    private lateinit var factory: Factory
    private val viewModel by viewModels<SearchViewModel> { factory }

    private lateinit var notesFilterAdapter: NotesFilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = Factory.getInstance(this)

        setListeners()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        notesFilterAdapter.listItemClicked.clear()
        viewModel.fetchNotes()
    }

    private fun setupViewModel() {
        viewModel.fetchNotes()
        viewModel.noteList.observe(this) {
            setContent(it)
        }
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener { finish() }

            btnSearch.setOnClickListener {
                val iSearchResult = Intent(this@NotesFilterActivity, SearchResultActivity::class.java)
                iSearchResult.putExtra("queryList", notesFilterAdapter.listItemClicked.distinct().toTypedArray())
                startActivity(iSearchResult)
            }
        }
    }

    private fun setContent(notesList: List<Note>) {
        binding.rvNotesSearch.apply {
            notesFilterAdapter = NotesFilterAdapter(context, notesList)

            layoutManager = GridLayoutManager(this@NotesFilterActivity, 2)
            adapter = notesFilterAdapter
        }
    }
}