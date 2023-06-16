package com.project.aromaloka.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.adapter.AllPerfumeAdapter
import com.project.aromaloka.databinding.ActivityAllPerfumeBinding
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.ResponseSession
import com.project.aromaloka.ui.auth.LoginActivity
import com.project.aromaloka.ui.main.BottomMainActivity
import com.project.aromaloka.utils.Factory
import kotlinx.coroutines.launch

class AllPerfumeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllPerfumeBinding
    private lateinit var factory: Factory
    private lateinit var perfumeAdapter: AllPerfumeAdapter
    private lateinit var token: String
    private val viewModel by viewModels<AllPerfumeViewModel> { factory }
    private var perfumeList: MutableList<Perfume> = mutableListOf()
    private var selectedPerfume: Perfume? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllPerfumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = Factory.getInstance(this)

        val session = intent.getParcelableExtra<ResponseSession>("session")
        if (session != null) {
            lifecycleScope.launch {
                viewModel.saveSession(session)
            }
        }

        viewModel.getSession().observe(this) { response ->
            val session = response as? ResponseSession
            if (session != null) {
                token = session.token
                if (!session.isLogin) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                } else {
                    viewModel.fetchPerfumeData(token)
                }
            }
        }

        setupAdapter()
        setRecyclerView()
        observePerfumeData()

        binding.btnFinish.setOnClickListener {
                val intent = Intent(this@AllPerfumeActivity, BottomMainActivity::class.java)
                intent.putExtra("session", session)
                setResult(RESULT_OK, intent)
                finish()
        }
    }

    private fun setupAdapter() {
        perfumeAdapter = AllPerfumeAdapter(this, perfumeList) { perfume ->
            selectedPerfume = perfume
            perfumeAdapter.notifyDataSetChanged()

            if (selectedPerfume != null) {
                viewModel.addFavorite(token, selectedPerfume!!.id)
            } else {
                viewModel.removeFavorite(token, perfume.id)
            }
        }
    }

    private fun setRecyclerView() {
        binding.rvBrandSearch.apply {
            layoutManager = LinearLayoutManager(this@AllPerfumeActivity)
            adapter = perfumeAdapter
        }
    }

    private fun observePerfumeData() {
        viewModel.perfumeList.observe(this) { perfumeList ->
            this.perfumeList.clear()
            var perfumeListSorted = perfumeList.sortedBy{ it.variant}
            this.perfumeList.addAll(perfumeListSorted)
            perfumeAdapter.notifyDataSetChanged()
        }
    }
}

