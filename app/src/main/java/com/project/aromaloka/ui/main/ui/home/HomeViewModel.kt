package com.project.aromaloka.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.Repository
import com.project.aromaloka.models.ResponseSession
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {
    private val _perfumeList: LiveData<List<Perfume>> = repository.responseAllPerfume
    val perfumeList: LiveData<List<Perfume>> = _perfumeList

    private val _favoriteList: LiveData<List<String>> = repository.userFavoritesResponse
    val favoriteList: LiveData<List<String>> = _favoriteList

    private val _recommendationList: LiveData<List<String>> = repository.perfumeRecommendationsResponse
    val recommendationList: LiveData<List<String>> = _recommendationList


    fun fetchPerfumeData(token: String) {
        viewModelScope.launch {
            repository.getAllPerfume(token)
        }
    }

    fun getRecommendation(perfumeId: String) {
        viewModelScope.launch{
            repository.getPerfumeRecommendations(perfumeId)
        }
    }


    fun getPerfumeById(perfumeId: String): Perfume? {
        val allPerfumes = repository.responseAllPerfume.value
        return allPerfumes?.find { perfume ->
            perfume.id == perfumeId
        }
    }



    fun getSession(): LiveData<ResponseSession> {
        val liveData = MutableLiveData<ResponseSession>()

        viewModelScope.launch {
            repository.getSession().collect { responseSession ->
                liveData.value = responseSession
            }
        }

        return liveData
    }

    fun logout() = viewModelScope.launch { repository.logout() }

    suspend fun saveSession(session: ResponseSession) {
        repository.saveSession(session)
    }

    fun getFavoritePerfume(token: String){
        viewModelScope.launch{
            repository.getCurrentUserFavorites(token)
        }
    }

}





