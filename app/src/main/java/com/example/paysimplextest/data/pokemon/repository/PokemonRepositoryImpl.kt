package com.example.paysimplextest.data.login.repository

import android.content.ContentValues.TAG
import android.util.Log
import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.api.PokemonApi
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import com.example.paysimplextest.domain.common.base.BaseResult
import com.example.paysimplextest.domain.login.PokemonRepository
import com.example.paysimplextest.domain.login.entity.PokemonEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PokemonRepositoryImpl @Inject constructor(private val pokemonApi: PokemonApi) : PokemonRepository {
    override suspend fun pokemon(): Flow<BaseResult<PokemonEntity, WrappedResponse<PokemonResponse>>> {
        return flow {
            val response = pokemonApi.data()
            if(response.isSuccessful){
                val body = response.body()!!
                Log.d(TAG, "pokemon: ${response.body()}")
                val pokemonEntity = PokemonEntity(body.count, body.next, body.results!!)
                emit(BaseResult.Success(pokemonEntity))
            }else{
                val type = object : TypeToken<WrappedResponse<PokemonResponse>>(){}.type
                val err : WrappedResponse<PokemonResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
//                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}