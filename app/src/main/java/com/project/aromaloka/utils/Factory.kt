package com.project.aromaloka.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.aromaloka.models.Repository
import com.project.aromaloka.ui.auth.LoginViewModel
import com.project.aromaloka.ui.auth.RegisterViewModel
import com.project.aromaloka.ui.detail.BrandDetailViewModel
import com.project.aromaloka.ui.detail.PerfumeDetailViewModel
import com.project.aromaloka.ui.main.ui.favorite.FavoriteViewModel
import com.project.aromaloka.ui.main.ui.home.AllPerfumeViewModel
import com.project.aromaloka.ui.main.ui.home.HomeViewModel
import com.project.aromaloka.ui.main.ui.search.searchresult.SearchViewModel

class Factory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(PerfumeDetailViewModel::class.java) -> {
                PerfumeDetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }

            modelClass.isAssignableFrom(BrandDetailViewModel::class.java) -> {
                BrandDetailViewModel(repository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AllPerfumeViewModel::class.java) -> {
                AllPerfumeViewModel(repository) as T
            }



            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: Factory? = null
        fun getInstance(context: Context): Factory {
            return instance ?: synchronized(this) {
                instance ?: Factory(Injection.Companion.provideRepository(context))
            }.also { instance = it }
        }
    }
}


