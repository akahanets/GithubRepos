package com.example.githubtest.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RepoOwner(
        @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("login") val username: String,
        @Expose @SerializedName("avatar_url") val avatar: String
): Parcelable
