package com.warba.data.model.remote

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("protocol") val protocol: String,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String? = "",
    @SerializedName("url") val submittedUrl: String
)
