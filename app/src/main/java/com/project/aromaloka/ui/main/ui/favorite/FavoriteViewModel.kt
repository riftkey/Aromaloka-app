package com.project.aromaloka.ui.main.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.Repository
import com.project.aromaloka.models.UserFavoritesResponse
import kotlinx.coroutines.launch

class FavoriteViewModel (private val repository: Repository) : ViewModel() {
    private var _sessionToken = MutableLiveData<String>()
    val sessionToken: LiveData<String> = _sessionToken
    val isFavorite: MutableLiveData<Boolean> = repository.isFavorite as MutableLiveData<Boolean>

    private val _responseFavorite: MutableLiveData<List<String>?> = MutableLiveData()
    val responseFavorite: LiveData<List<String>?> = _responseFavorite

    val userFavoritesResponse: LiveData<List<String>> = repository.userFavoritesResponse

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collect { responseSession ->
                _sessionToken.value = responseSession.token
            }
        }
    }

    fun getPerfumeById(perfumeId: String): Perfume? {
        val allPerfumes = repository.responseAllPerfume.value
        return allPerfumes?.find { perfume ->
            perfume.id == perfumeId
        }
    }

    fun getFavoritePerfume(token: String){
        viewModelScope.launch{
            repository.getCurrentUserFavorites(token)
        }
    }

    fun removeFavorite(token: String, perfumeId: String) {
        viewModelScope.launch {
            repository.removeFavorite(token, perfumeId)
            isFavorite.postValue(false)
            _responseFavorite.postValue(repository.userFavoritesResponse.value)
        }
    }

}