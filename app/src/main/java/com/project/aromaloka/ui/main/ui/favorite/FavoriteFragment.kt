package com.project.aromaloka.ui.main.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.aromaloka.adapter.FavoritedPerfumeAdapter
import com.project.aromaloka.databinding.FragmentFavoriteBinding
import com.project.aromaloka.ui.detail.PerfumeDetailActivity
import com.project.aromaloka.utils.Factory

class FavoriteFragment : Fragment() {


    private lateinit var progressBar: ProgressBar

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var factory: Factory
    private lateinit var token: String
    private val viewModel by viewModels<FavoriteViewModel> { factory }
    private lateinit var favoriteAdapter: FavoritedPerfumeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        factory = Factory.getInstance(requireContext())

        progressBar = binding.progressBar

        viewModel.getSession()
        token = viewModel.sessionToken.value.toString()

        setupAdapter()

        binding.rvPerfumeFav.adapter = favoriteAdapter
        binding.rvPerfumeFav.layoutManager = LinearLayoutManager(requireContext())


        token?.let{ viewModel.getFavoritePerfume(it)}

        Log.d("Debug","onCreate Favorite")
        setFavoritedContent()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

    }

    private fun setupAdapter() {
        favoriteAdapter = FavoritedPerfumeAdapter(
            listPerfume = mutableListOf(),
            listener = { perfume ->
                val intent = Intent(requireContext(), PerfumeDetailActivity::class.java)
                intent.putExtra("perfume", perfume)
                startActivity(intent)
            },
            removeListener = { perfume ->
                viewModel.removeFavorite(token, perfume.id)
                token?.let{ viewModel.getFavoritePerfume(it)}
                setFavoritedContent()
            }
        )
    }

    private fun setFavoritedContent() {
        progressBar.visibility = View.VISIBLE
        viewModel.userFavoritesResponse.observe(viewLifecycleOwner) { favoritesResponse ->
            favoritesResponse?.let { perfumeIds ->
                val favoritePerfumes = perfumeIds.mapNotNull { perfumeId ->
                    viewModel.getPerfumeById(perfumeId)
                }.toMutableList()

                favoriteAdapter.setData(favoritePerfumes)


                progressBar.visibility = View.GONE
            }
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    override fun onResume() {
        super.onResume()
        Log.d("Debug","onResume Favorite")
        token?.let{ viewModel.getFavoritePerfume(it)}
        setFavoritedContent()
    }
}