package com.project.aromaloka.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.aromaloka.service.ApiConfig
import com.project.aromaloka.service.ApiMLService
import com.project.aromaloka.service.ApiService
import com.project.aromaloka.utils.SessionPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

class Repository internal constructor(
    private val preferences: SessionPreferences,
    private val service: ApiService,
    private val mlService: ApiMLService
){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> =_isLoading

    private val _responseRegister = MutableLiveData<ResponseRegister>()
    val responseRegister: LiveData<ResponseRegister> = _responseRegister

    private val _responseLogin = MutableLiveData<ResponseLogin?>()
    val responseLogin: MutableLiveData<ResponseLogin?> = _responseLogin

    private val _responseAllPerfume = MutableLiveData<List<Perfume>>()
    val responseAllPerfume: LiveData<List<Perfume>> = _responseAllPerfume

    private val _responseVariantPerfume = MutableLiveData<Perfume>()
    val responseVariantPerfume: LiveData<Perfume> = _responseVariantPerfume

    private val _uniqueBrandNames = MutableLiveData<Set<String>>()
    val uniqueBrandNames: LiveData<Set<String>> = _uniqueBrandNames

    private val _responseAllBrand = MutableLiveData<List<Brand>>()
    val responseAllBrand: LiveData<List<Brand>> = _responseAllBrand

    private val _responseAllNote = MutableLiveData<List<Note>>()
    val responseAllNote: LiveData<List<Note>> = _responseAllNote

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _perfumeRecommendationsResponse = MutableLiveData<List<String>>()
    val perfumeRecommendationsResponse: LiveData<List<String>> = _perfumeRecommendationsResponse

    private val _userFavoritesResponse = MutableLiveData<List<String>>()
    val userFavoritesResponse: LiveData<List<String>> = _userFavoritesResponse

    private val _responsePerfumeVariants = MutableLiveData<List<Perfume>?>()
    val responsePerfumeVariants: MutableLiveData<List<Perfume>?> = _responsePerfumeVariants

    fun postRegister(name: String, email: String, password: String) {
        _isLoading.value = true
        val response = ApiConfig.getApiService().postRegister(name, email, password)
        response.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                _isLoading.postValue(false)
                val responseBody = response.body()
                if (responseBody != null && response.isSuccessful) {
                    _responseRegister.value = response.body()
                    Log.d(TAG, "Response Message: ${response.message()}")
                } else {
                    Log.e(
                        TAG,
                        "onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }
            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun postLogin(email: String, password: String){
        _isLoading.value = true
        val response = ApiConfig.getApiService().postLogin(email, password)
        response.enqueue(object: Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                _isLoading.postValue(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null && !responseBody.error) {
                    _responseLogin.value = responseBody
                } else {
                    Log.e(
                        TAG,
                        "Login onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            }
            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    suspend fun getAllPerfume(token: String) {
        _isLoading.postValue(true)
        try {
            val response = service.getAllPerfumes("Bearer $token")
            if (response.isSuccessful) {
                _responseAllPerfume.postValue(response.body())
            } else {
                Log.e(
                    TAG,
                    "Repository GetAllPerfume onFailure: ${response.message()}, ${response.body()?.toString()}"
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "GetAllPerfume onFailure: ${e.message}", e)
        } finally {
            _isLoading.postValue(false)
        }
    }


    suspend fun getVariantPerfume(variant: String) {
        _isLoading.postValue(true)
        try {
            val response = service.getPerfumeByName(variant)
            if (response.isSuccessful) {
                val perfumeList = response.body()
                if (perfumeList != null && perfumeList.isNotEmpty()) {
                    _responseVariantPerfume.postValue(perfumeList[0])
                }
            } else {
                Log.e(
                    TAG,
                    "Repository getVariantPerfume onFailure: ${response.message()}, ${response.body()?.toString()}"
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "getVariantPerfume onFailure: ${e.message}", e)
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun addFavorite(token: String, perfumeId: String) {
        coroutineScope {
            try {
                val response = ApiConfig.getApiService().addFavorite("Bearer $token", perfumeId)
                if (response.isSuccessful) {
                    _isFavorite.postValue(true)
                    Log.d("Favorite", "Add favorite successful")
                } else {
                    Log.e(
                        TAG,
                        "addFavorite onFailure: ${response.message()}, ${response.body()?.message.toString()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("Favorite", "Add favorite failed: ${e.message}")
            }
        }
    }

    suspend fun removeFavorite(token: String, perfumeId: String) {
        Log.d("Favorite", "Remove favorite - Token: $token, PerfumeId: $perfumeId")
        coroutineScope {
            try {
                val response = ApiConfig.getApiService().removeFavorite("Bearer $token", perfumeId)
                if (response.isSuccessful) {
                    _isFavorite.postValue(false)
                    Log.d("Favorite", "Remove favorite successful")
                } else {
                    Log.e("Favorite", "Remove favorite failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("Favorite", "Remove favorite failed: ${e.message}")
            }
        }
    }


    suspend fun getCurrentUserFavorites(token: String) {
        Log.d("Favorite", "Get favorite - Token: $token")
        try {
            val response = ApiConfig.getApiService().getCurrentUserFavorites("Bearer $token")
            Log.d("API", "getCurrentUserFavorites response: ${response.code()}")
            if (response.isSuccessful) {
                val favorites = response.body()
                _userFavoritesResponse.postValue(favorites)
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API", "getCurrentUserFavorites failed: ${response.code()}, Error Body: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("API", "getCurrentUserFavorites failed: ${e.message}")
        }
    }


    suspend fun getPerfumeRecommendations(perfumeId: String): List<String>? {
        try {
            val response = mlService.getRecommendations(perfumeId)
            Log.d("API", "getPerfumeRecommendations response: ${response.code()}")
            if (response.isSuccessful) {
                Log.d("API", "getPerfumeRecommendations response: ${response.body()}")
                val recommendationResponse = response.body()
                _perfumeRecommendationsResponse.postValue(recommendationResponse?.similarPerfumeIds)
                return recommendationResponse?.similarPerfumeIds?.map { it }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API", "getPerfumeRecommendations failed: ${response.code()}, Error Body: $errorBody")
            }
        } catch (e: Exception) {
            Log.e("API", "getPerfumeRecommendations failed: ${e.message}")
        }
        return null
    }




    suspend fun getPerfumeVariantsByBrand(brand: String) {
        _isLoading.value = true
        try {
            val response = ApiConfig.getApiService().getVariantsByBrand(brand)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    _responsePerfumeVariants.postValue(responseBody)
                }
            } else {
                Log.e(
                    TAG,
                    "getPerfumeVariantsByBrand onFailure: ${response.message()}, ${response.body()?.toString()}"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "getPerfumeVariantsByBrand onFailure: ${e.message}", e)
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllBrand() {
        _isLoading.postValue(true)
        try {
            val response = service.getAllBrands()
            if (response.isSuccessful) {
                _responseAllBrand.postValue(response.body())
            } else {
                Log.e(
                    TAG,
                    "Repository GETALLBRAND onFailure: ${response.message()}, ${response.body()?.toString()}"
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "GETALLBRAND onFailure: ${e.message}", e)
        } finally {
            _isLoading.postValue(false)
        }
    }

    suspend fun getAllNotes() {
        _isLoading.postValue(true)
        try {
            val response = service.getAllNotes()
            if (response.isSuccessful) {
                _responseAllNote.postValue(response.body())
            } else {
                Log.e(
                    TAG,
                    "Repository GetAllNote onFailure: ${response.message()}, ${response.body()?.toString()}"
                )
            }
        } catch (e: Exception) {
            Log.e("Repository", "GetAllNote onFailure: ${e.message}", e)
        } finally {
            _isLoading.postValue(false)
        }
    }



    fun getSession(): Flow<ResponseSession> {
        return preferences.getSession() as Flow<ResponseSession>
    }


    suspend fun saveSession(ss: ResponseSession){
        preferences.saveSession(ss)
    }

    suspend fun login() = preferences.login()
    suspend fun logout() = preferences.logout()


    companion object {
        private const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preferences: SessionPreferences,
            service: ApiService,
            mlService: ApiMLService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preferences, service, mlService)
            }.also { instance = it }
    }

}