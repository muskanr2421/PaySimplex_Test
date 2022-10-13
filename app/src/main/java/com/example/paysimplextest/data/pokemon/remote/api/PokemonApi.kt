package com.example.paysimplextest.data.login.remote.api

import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PokemonApi {

    @GET("pokemon")
    suspend fun data() : Response<WrappedResponse<PokemonResponse>>

}