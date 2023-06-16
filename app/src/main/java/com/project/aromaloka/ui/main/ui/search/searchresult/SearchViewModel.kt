package com.project.aromaloka.ui.main.ui.search.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Brand
import com.project.aromaloka.models.Note
import com.project.aromaloka.models.Repository
import kotlinx.coroutines.launch

class SearchViewModel (private val repository: Repository) : ViewModel() {
    private val _brandList: LiveData<List<Brand>> = repository.responseAllBrand
    val brandList: LiveData<List<Brand>> = _brandList

    private val _noteList: LiveData<List<Note>> = repository.responseAllNote
    val noteList: LiveData<List<Note>> = _noteList

    fun fetchBrands() {
        viewModelScope.launch {
            repository.getAllBrand()
        }
    }

    fun fetchNotes() {
        viewModelScope.launch {
            repository.getAllNotes()
        }
    }
}





