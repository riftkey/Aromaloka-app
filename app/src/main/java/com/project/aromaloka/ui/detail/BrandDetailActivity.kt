package com.project.aromaloka.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.project.aromaloka.R
import com.project.aromaloka.adapter.BrandPerfumeAdapter
import com.project.aromaloka.adapter.PerfumeAdapter
import com.project.aromaloka.databinding.ActivityBrandDetailBinding
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.utils.Factory

class BrandDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBrandDetailBinding
    private lateinit var perfumeAdapter: BrandPerfumeAdapter
    private lateinit var factory: Factory
    private val viewModel: BrandDetailViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBrandDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = Factory.getInstance(this)
        setAdapter()

        val brandName = intent.getStringExtra("EXTRA_BRAND_NAME")
        if (brandName != null) {
            setBrandRecyclerView()
            viewModel.fetchPerfumeData(brandName)
            observePerfumeList()
        }

        setListeners()

        binding.apply {
            tvBrandName.text = brandName
            val cleanedBrandName = brandName?.replace(" ", "")?.replace("&", "")?.replace(".", "")?.replace("'", "")?.toLowerCase()
            val brandLogoResId = resources.getIdentifier(cleanedBrandName, "drawable", packageName)
            if (brandLogoResId != 0) {
                ivBrand.setImageResource(brandLogoResId)
            } else {
                ivBrand.setImageResource(R.drawable.fonce)
            }
        }





        viewModel.perfumeList.observe(this, Observer { perfumeVariants ->
            setBrandRecyclerView()
            binding.btnBrandInsta.setOnClickListener {
                if (!perfumeVariants.isNullOrEmpty()) {
                    val instagramLink = perfumeVariants[0].instagram_link
                    instagramLink?.let { link ->
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        startActivity(intent)
                    }
                }
            }
        })
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun setAdapter(){
        perfumeAdapter = BrandPerfumeAdapter { perfume ->
            val intent = Intent(this@BrandDetailActivity, PerfumeDetailActivity::class.java)
            intent.putExtra("perfume", perfume)
            startActivity(intent)
        }
    }
    private fun setBrandRecyclerView() {

        binding.rvBrandPerfume.apply {
            layoutManager = GridLayoutManager(this@BrandDetailActivity, 2)
            adapter = perfumeAdapter
        }
    }

    private fun observePerfumeList() {
        viewModel.perfumeList.observe(this) { perfumes ->
            val mutablePerfumes = perfumes?.toMutableList()
            if (mutablePerfumes != null) {
                perfumeAdapter.setData(mutablePerfumes)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



}

