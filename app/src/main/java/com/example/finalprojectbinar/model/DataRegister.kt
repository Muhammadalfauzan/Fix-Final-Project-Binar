package com.example.finalprojectbinar.model


import com.google.gson.annotations.SerializedName

data class DataRegister(
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("expiredAt")
    val expiredAt: String?
)