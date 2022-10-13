package com.example.paysimplextest.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("next") var next : String? = null,
    @SerializedName("previous") var previous : String? = null,
    @SerializedName("results") var results: ArrayList<Result>? = null
)

data class Result(
    @SerializedName("name") var name : String? = null,
)