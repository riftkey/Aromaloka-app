package com.project.aromaloka.ui.main.ui.search.filter

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.adapter.BrandFilterAdapter
import com.project.aromaloka.databinding.ActivityBrandFilterBinding
import com.project.aromaloka.models.Brand
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchViewModel
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchResultActivity
import com.project.aromaloka.utils.Factory

@RequiresApi(Build.VERSION_CODES.M)
class BrandFilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrandFilterBinding
    private lateinit var factory: Factory
    private val viewModel by viewModels<SearchViewModel> { factory }
    private lateinit var brandFilterAdapter: BrandFilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandFilterBinding.inflate(layoutInflater)
        factory = Factory.getInstance(this)
        setContentView(binding.root)
        setListeners()
        setContent()
        observeSearchData()
    }

    override fun onResume() {
        super.onResume()
        brandFilterAdapter.listItemClicked.clear()
        viewModel.fetchBrands()
    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }

            btnSearch.setOnClickListener {
                val iSearchResult = Intent(this@BrandFilterActivity, SearchResultActivity::class.java)
                iSearchResult.putExtra("queryList", brandFilterAdapter.listItemClicked.distinct().toTypedArray())
                startActivity(iSearchResult)
            }
        }
    }

    private fun setContent() {
        binding.rvBrandSearch.apply {
            brandFilterAdapter = BrandFilterAdapter(this@BrandFilterActivity )

            layoutManager = LinearLayoutManager(this@BrandFilterActivity)
            adapter = brandFilterAdapter
        }
    }

    private fun observeSearchData() {
        viewModel.brandList.observe(this) { brandList ->
            brandFilterAdapter.setData(brandList as MutableList<Brand>)
        }
    }
}