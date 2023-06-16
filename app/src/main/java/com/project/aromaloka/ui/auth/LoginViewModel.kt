package com.project.aromaloka.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Repository
import com.project.aromaloka.models.ResponseLogin
import com.project.aromaloka.models.ResponseSession
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: Repository) : ViewModel() {
    val responseLogin: MutableLiveData<ResponseLogin?> = repository.responseLogin
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postLogin(email: String, password: String){
        viewModelScope.launch{
            repository.postLogin(email, password)
        }
    }

    fun saveSession(ss: ResponseSession){
        viewModelScope.launch{
            repository.saveSession(ss)
        }
    }


    fun login(){
        viewModelScope.launch { repository.login() }
    }
}