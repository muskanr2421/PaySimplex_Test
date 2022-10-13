package com.example.paysimplextest.domain.login.usecase

import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import com.example.paysimplextest.domain.common.base.BaseResult
import com.example.paysimplextest.domain.login.PokemonRepository
import com.example.paysimplextest.domain.login.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonUseCase @Inject constructor(private val pokemonRepository: PokemonRepository) {
    suspend fun execute(): Flow<BaseResult<PokemonEntity, WrappedResponse<PokemonResponse>>> {
        return pokemonRepository.pokemon()
    }

}