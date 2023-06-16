package com.project.aromaloka.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note(
    var name: String = "Bergamot",
    var isClicked: Boolean = false
) : Parcelable
