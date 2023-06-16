package com.project.aromaloka.models

data class ResponseBrand(
    val brandName: String,
    val instagram_url: String,
    val perfumes: List<Perfume>
)
