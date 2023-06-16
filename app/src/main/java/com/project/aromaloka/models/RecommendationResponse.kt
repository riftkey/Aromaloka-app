package com.project.aromaloka.models

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(
    @SerializedName("similiar perfume id")
    val similarPerfumeIds: List<String>
)

