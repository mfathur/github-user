package com.mfathurz.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val avatar: String,
    val company: String,
    val location: String,
    val name: String,
    val repository: Int,
    val username: String
) : Parcelable