package com.example.paysimplextest.data.common.utils

import com.example.paysimplextest.domain.login.entity.Results
import com.google.gson.annotations.SerializedName

data class WrappedResponse<T> (
    @SerializedName("count") var count : Int,
    @SerializedName("next") var next : String,
//    @SerializedName("previous") var previous : String ?= null,
    @SerializedName("results") var results : ArrayList<Results>? = null
)

