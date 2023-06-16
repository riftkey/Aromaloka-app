package com.project.aromaloka.service

import com.project.aromaloka.models.*
import retrofit2.Response
import retrofit2.http.*

interface ApiMLService {

    @GET("recommendation/")
    suspend fun getRecommendations(@Query("id") perfumeId: String): Response<RecommendationResponse>

}
