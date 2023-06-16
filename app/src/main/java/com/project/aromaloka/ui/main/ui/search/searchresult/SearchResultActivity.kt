package com.project.aromaloka.ui.main.ui.search.searchresult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.project.aromaloka.adapter.SearchPagerAdapter
import com.project.aromaloka.databinding.ActivitySearchResultBinding
import com.project.aromaloka.ui.main.ui.search.SearchFragment
import com.project.aromaloka.ui.main.ui.search.filter.FilterActivity

class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var searchQuery: String
    private lateinit var listSearchQuery: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        retrieveQuery()
        setListeners()
        setContent()

    }

    private fun retrieveQuery() {
        searchQuery = intent.getStringExtra("query") ?: ""
        listSearchQuery = intent.getStringArrayExtra("queryList") ?: emptyArray()

    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setContent() {
        val viewPager = binding.viewPager
        val tabLayout = binding.layoutTab

        val adapter = SearchPagerAdapter(this, searchQuery, listSearchQuery)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = SearchFragment.titleArray[position]
        }.attach()
    }
}





