package com.example.paysimplextest.domain.login.entity

import com.example.paysimplextest.data.login.remote.dto.Result


data class PokemonEntity(
    var count: Int,
    var next: String,
//    var previous: String,
    var results: ArrayList<Results>?
)

data class Results(
    var name: String,
)