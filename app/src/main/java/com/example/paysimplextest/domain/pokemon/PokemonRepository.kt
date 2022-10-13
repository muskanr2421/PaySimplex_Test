package com.example.paysimplextest.domain.login

import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import com.example.paysimplextest.domain.common.base.BaseResult
import com.example.paysimplextest.domain.login.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    suspend fun pokemon() : Flow<BaseResult<PokemonEntity, WrappedResponse<PokemonResponse>>>
}