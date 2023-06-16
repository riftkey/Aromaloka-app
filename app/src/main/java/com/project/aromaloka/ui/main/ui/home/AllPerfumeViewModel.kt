package com.project.aromaloka.ui.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Perfume
import com.project.aromaloka.models.Repository
import com.project.aromaloka.models.ResponseSession
import kotlinx.coroutines.launch

class AllPerfumeViewModel(private val repository: Repository) : ViewModel() {
    private val _perfumeList: LiveData<List<Perfume>> = repository.responseAllPerfume
    val perfumeList: LiveData<List<Perfume>> = _perfumeList

    fun fetchPerfumeData(token: String) {
        viewModelScope.launch {
            repository.getAllPerfume(token)
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

    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()

    fun addFavorite(token: String, perfumeId: String) {
        viewModelScope.launch {
            repository.addFavorite(token, perfumeId)
            isFavorite.postValue(true)
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
        }
    }


    suspend fun saveSession(session: ResponseSession) {
        repository.saveSession(session)
    }
}