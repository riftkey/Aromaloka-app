package com.project.aromaloka.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResponseSession(
    val name: String,
    val token: String,
    val isLogin: Boolean
) : Parcelable
