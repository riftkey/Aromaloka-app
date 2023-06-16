package com.project.aromaloka.models

import com.google.gson.annotations.SerializedName

data class ResponseRegister (
    @field:SerializedName("message")
    val message: String,

    @field: SerializedName("userId")
    val userId : String
)
