package com.project.aromaloka.ui.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.project.aromaloka.R
import com.project.aromaloka.databinding.ActivityPerfumeDetailBinding
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.utils.Factory

class PerfumeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerfumeDetailBinding
    private lateinit var factory: Factory
    private val viewModel: PerfumeDetailViewModel by viewModels { factory }
    private var isFavorite: Boolean = false
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var perfumeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfumeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = Factory.getInstance(this)

        val perfume = intent.getParcelableExtra<Perfume>("perfume")
        val variant = perfume?.variant
        perfumeId = perfume!!.id

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        viewModel.isFavorite.observe(this, Observer { it ->
            isFavorite = it
        })

        updateFavoriteButtonState()



        variant?.let {
            lifecycleScope.launch {
                viewModel.getVariantPerfume(variant)
            }
        }

        viewModel.getSession()

        setListeners()

        binding.btnFav.setOnClickListener {
            val perfumeId = viewModel.responseVariantPerfume.value?.id
            perfumeId?.let { id ->
                val token = viewModel.sessionToken.value
                if (token != null) {
                    if (isFavorite) {
                        viewModel.removeFavorite(token, id)
                        isFavorite = false
                        updateFavoriteButtonState()
                    } else {
                        viewModel.addFavorite(token, id)
                        isFavorite = true
                        updateFavoriteButtonState()
                    }
                    sharedPreferences.edit().putBoolean(perfumeId, isFavorite).apply()
                } else {
                    Toast.makeText(this, "Please log in to add favorite.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.responseVariantPerfume.observe(this, Observer { perfume ->

            binding.apply {
                Glide.with(this@PerfumeDetailActivity)
                    .load(perfume.variant_image_url)
                    .into(binding.ivPerfume)
                tvPerfumeName.text = perfume.variant
                tvPerfumeBrand.text = "by ${perfume.brand}"
                tvPerfumeDesc.text = perfume.concentration
                tvPrice.text = "Rp${perfume.price}"
                tvMl.text = "${perfume.size} ml"
                tvRating.text = perfume.rating

                val topNotes = listOf(
                    perfume.top_notes1,
                    perfume.top_notes2,
                    perfume.top_notes3
                ).filter { it.isNotBlank() }
                val middleNotes = listOf(
                    perfume.mid_notes1,
                    perfume.mid_notes2,
                    perfume.mid_notes3
                ).filter { it.isNotBlank() }
                val baseNotes = listOf(
                    perfume.base_notes1,
                    perfume.base_notes2,
                    perfume.base_notes3
                ).filter { it.isNotBlank() }

                topNotes.forEachIndexed { index, note ->
                    when (index) {
                        0 -> {
                            tvTopnotes1.text = note
                            llTopnotes1.visibility = View.VISIBLE
                        }
                        1 -> {
                            tvTopnotes2.text = note
                            llTopnotes2.visibility = View.VISIBLE
                        }
                        2 -> {
                            tvTopnotes3.text = note
                            llTopnotes3.visibility = View.VISIBLE
                        }
                    }
                }

                middleNotes.forEachIndexed { index, note ->
                    when (index) {
                        0 -> {
                            tvMidnotes1.text = note
                            llMidnotes1.visibility = View.VISIBLE
                        }
                        1 -> {
                            tvMidnotes2.text = note
                            llMidnotes2.visibility = View.VISIBLE
                        }
                        2 -> {
                            tvMidnotes3.text = note
                            llMidnotes3.visibility = View.VISIBLE
                        }
                    }
                }

                baseNotes.forEachIndexed { index, note ->
                    when (index) {
                        0 -> {
                            tvBasenotes1.text = note
                            llBasenotes1.visibility = View.VISIBLE
                        }
                        1 -> {
                            tvBasenotes2.text = note
                            llBasenotes2.visibility = View.VISIBLE
                        }
                        2 -> {
                            tvBasenotes3.text = note
                            llBasenotes3.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })

        viewModel.sessionToken.observe(this, Observer { token ->
            if (token != null) {
                updateFavoriteButtonState()
            }
        })

        viewModel.isFavorite.observe(this, Observer { isFavorite ->
            this.isFavorite = isFavorite
            updateFavoriteButtonState()
        })

    }

    private fun setListeners() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }

            tvPerfumeBrand.setOnClickListener {
                val perfume = intent.getParcelableExtra<Perfume>("perfume")
                val brand = perfume?.brand
                val iBrand = Intent(this@PerfumeDetailActivity, BrandDetailActivity::class.java)
                val brandName = brand
                iBrand.putExtra("EXTRA_BRAND_NAME", brandName)
                startActivity(iBrand)
            }

            btnBuy.setOnClickListener {
                val variantLink = viewModel.responseVariantPerfume.value?.variant_link
                variantLink?.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                    startActivity(intent)
                }
            }
        }

    }


    override fun onResume() {
        super.onResume()
        viewModel.isFavorite.observe(this, Observer { isFavorite ->
            this.isFavorite = isFavorite
            updateFavoriteButtonState()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun updateFavoriteButtonState() {
        if (isFavorite) {
            binding.btnFav.setImageResource(R.drawable.fav_fill)
            binding.btnFav.isSelected = true
        } else {
            binding.btnFav.setImageResource(R.drawable.fav)
            binding.btnFav.isSelected = false
        }
    }
}
