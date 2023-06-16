package com.project.aromaloka.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.*
import kotlinx.coroutines.launch

class PerfumeDetailViewModel(private val repository: Repository) : ViewModel() {
    val responseVariantPerfume: LiveData<Perfume> = repository.responseVariantPerfume
    private var _sessionToken = MutableLiveData<String>()
    val sessionToken: LiveData<String> = _sessionToken
    val isFavorite: MutableLiveData<Boolean> = repository.isFavorite as MutableLiveData<Boolean>

    fun getVariantPerfume(variant: String) {
        viewModelScope.launch {
            repository.getVariantPerfume(variant)
        }
    }

    fun addFavorite(token: String, perfumeId: String) {
        viewModelScope.launch {
            repository.addFavorite(token, perfumeId)
            isFavorite.postValue(true)
        }
    }



    fun removeFavorite(token: String, perfumeId: String) {
        viewModelScope.launch {
            repository.removeFavorite(token, perfumeId)
            isFavorite.postValue(false)
        }
    }


    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { responseSession ->
                _sessionToken.value = responseSession.token
            }
        }
    }
}
