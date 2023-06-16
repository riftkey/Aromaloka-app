package com.project.aromaloka.ui.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.R
import com.project.aromaloka.adapter.PerfumeAdapter
import com.project.aromaloka.adapter.PopularPerfumeAdapter
import com.project.aromaloka.databinding.FragmentHomeBinding
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.ResponseSession
import com.project.aromaloka.ui.auth.LoginActivity
import com.project.aromaloka.ui.detail.PerfumeDetailActivity
import com.project.aromaloka.ui.main.BottomMainActivity
import com.project.aromaloka.utils.Factory
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var factory: Factory
    private val binding get() = _binding!!
    private lateinit var token: String
    private val viewModel by viewModels<HomeViewModel> { factory }
    private lateinit var perfumeAdapter: PerfumeAdapter
    private lateinit var popularPerfumeAdapter: PopularPerfumeAdapter
    private var recommendationId: List<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        factory = Factory.getInstance(requireContext())

        var session = arguments?.getParcelable("session") as? ResponseSession
        token = session?.token ?: ""



        if (session != null) {
            lifecycleScope.launch {
                viewModel.saveSession(session!!)
            }
        }



        token.let { viewModel.getFavoritePerfume(it) }
        viewModel.favoriteList.observe(viewLifecycleOwner) { response ->
            if (response.isNullOrEmpty()) {
                val intent = Intent(requireActivity(), AllPerfumeActivity::class.java)
                Log.d("Debug", "OnCreate Method")
                launchSelectFavorite.launch(intent)
            }
        }



        setupAdapters()
        setHomeRecyclerView()
        observePerfumeData()
        viewModel.fetchPerfumeData(token)

        viewModel.getSession().observe(viewLifecycleOwner) { response ->
            val sessionResponse = response as? ResponseSession
            if (sessionResponse != null) {
                session = sessionResponse
                token = sessionResponse.token
                if (!sessionResponse.isLogin) {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                } else {
                    viewModel.fetchPerfumeData(token)
                    observePerfumeData()
                }
            }
        }

        viewModel.getSession().observe(viewLifecycleOwner) { response ->
            val sessionResponse = response as? ResponseSession
            if (sessionResponse != null) {
                session = sessionResponse
                token = sessionResponse.token
                if (!sessionResponse.isLogin) {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().finish()
                } else {
                    viewModel.fetchPerfumeData(token)
                }
            }
        }


        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

    }

    private fun showLogoutConfirmationDialog() {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.logout_alert, null)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setView(view)
            setCancelable(true)

            val alertDialog = create()
            alertDialog.show()

            val btnLogoutYes = view.findViewById<Button>(R.id.btn_logout_yes)
            btnLogoutYes.setOnClickListener {
                viewModel.logout()
                alertDialog.dismiss()
            }

            val btnLogoutNo = view.findViewById<Button>(R.id.btn_logout_no)
            btnLogoutNo.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    }

    private fun setupAdapters() {
        perfumeAdapter = PerfumeAdapter { perfume ->
            val intent = Intent(requireContext(), PerfumeDetailActivity::class.java)
            intent.putExtra("perfume", perfume)
            startActivity(intent)
        }

        popularPerfumeAdapter = PopularPerfumeAdapter { perfume ->
            val intent = Intent(requireContext(), PerfumeDetailActivity::class.java)
            intent.putExtra("perfume", perfume)
            startActivity(intent)
        }
    }

    private fun setHomeRecyclerView() {
        binding.rvForMe.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = perfumeAdapter
        }

        binding.rvPopular.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularPerfumeAdapter
        }
    }

    private fun observePerfumeData() {
        viewModel.recommendationList.observe(viewLifecycleOwner) { recommendationList ->
            recommendationId = recommendationList
            viewModel.fetchPerfumeData(token)
        }

        viewModel.perfumeList.observe(viewLifecycleOwner) { perfumeList ->
            val perfumeListSorted = perfumeList.sortedByDescending { it.rating }
            popularPerfumeAdapter.setData(perfumeListSorted as MutableList<Perfume>)

            recommendationId?.let {
                val listForYou = mutableListOf<Perfume>()
                it.forEach { id ->
                    val perfumeForYou = perfumeList.filter { it.id == id }
                    listForYou.addAll(perfumeForYou)
                }
                perfumeAdapter.setData(listForYou)
            }
        }

        viewModel.favoriteList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) viewModel.getRecommendation(it.random())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val launchSelectFavorite = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            token.let { viewModel.getFavoritePerfume(it) }
            viewModel.favoriteList.observeOnce(viewLifecycleOwner) { response ->
                if (response.isEmpty()) {
                    Log.d("Debug", "onMethod Method")
                } else {
                    startActivity(Intent(requireActivity(), BottomMainActivity::class.java))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        token?.let { viewModel.getFavoritePerfume(it) }
        setHomeRecyclerView()
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(value: T) {
                observer.onChanged(value)
                removeObserver(this)
            }
        })
    }
}
