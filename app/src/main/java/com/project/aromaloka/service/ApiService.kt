package com.project.aromaloka.service

import com.project.aromaloka.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("/register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseRegister>

    @FormUrlEncoded
    @POST("/login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @GET("/perfumes/")
    suspend fun getAllPerfumes(@Header("Authorization") token: String): Response<List<Perfume>>

    @GET("/perfume/{variant}")
    suspend fun getPerfumeByName(@Path("variant") variant: String): Response<List<Perfume>>

    @FormUrlEncoded
    @POST("add-favorite")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Field("perfumeId") perfumeId: String
    ): Response<ResponseFavorite>

    @FormUrlEncoded
    @POST("/remove-favorite")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Field("perfumeId") perfumeId: String
    ): Response<ResponseFavorite>

    @GET("/favorites")
    suspend fun getCurrentUserFavorites(
        @Header("Authorization") token: String): Response<List<String>>

    @GET("/perfume/brand/{brand}")
    suspend fun getVariantsByBrand(@Path("brand") brand: String): Response<List<Perfume>>

    @GET("/get-all-brand")
    suspend fun getAllBrands(): Response<List<Brand>>

    @GET("/notes")
    suspend fun getAllNotes(): Response<List<Note>>

    @GET("/recommendation/")
    suspend fun getRecommendations(@Query("id") perfumeId: String): Response<RecommendationResponse>



}
