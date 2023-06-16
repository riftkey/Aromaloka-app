package com.project.aromaloka.models

import com.google.gson.annotations.SerializedName

data class ResponseFavorite(
    @field:SerializedName("message")
    val message: String
)

data class UserFavoritesResponse(
    val favorites: List<String>
)


