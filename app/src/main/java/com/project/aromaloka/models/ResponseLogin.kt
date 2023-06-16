package com.project.aromaloka.models

class ResponseLogin (
    val error: Boolean,
    val message: String,
    val userId: String,
    val name: String,
    val idToken: String
)
