package com.project.aromaloka.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.Repository
import kotlinx.coroutines.launch

class BrandDetailViewModel(private val repository: Repository) : ViewModel() {
    val perfumeList: LiveData<List<Perfume>?> = repository.responsePerfumeVariants

    fun fetchPerfumeData(brand: String) {
        viewModelScope.launch {
            repository.getPerfumeVariantsByBrand(brand)
        }
    }
}
