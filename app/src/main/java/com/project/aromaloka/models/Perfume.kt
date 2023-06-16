package com.project.aromaloka.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Perfume(
    @SerializedName("size (ml)")
    val size: String,
    val gender: String,
    val mid_notes1: String,
    val price_per_ml: String,
    val rating: String,
    val concentration: String,
    val mid_notes3: String,
    val mid_notes2: String,
    val base_notes3: String,
    val top_notes3: String,
    val base_notes1: String,
    val base_notes2: String,
    val top_notes1: String,
    val top_notes2: String,
    val price: String,
    val instagram_link: String,
    val variant: String,
    val variant_image_url: String,
    val variant_link: String,
    val id: String,
    val brand: String,
    var isClicked: Boolean
)
: Parcelable

