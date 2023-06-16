package com.project.aromaloka.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.aromaloka.models.Repository
import com.project.aromaloka.models.ResponseRegister
import com.project.aromaloka.models.ResponseSession
import kotlinx.coroutines.launch

class RegisterViewModel (private val repository: Repository) : ViewModel(){
    val responseRegister: LiveData<ResponseRegister> = repository.responseRegister
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun postRegister(name: String, email: String, password: String){
        viewModelScope.launch{
            repository.postRegister(name, email, password)
        }
    }
}