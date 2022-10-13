package com.ydhnwb.cleanarchitectureexercise.data.login

import com.example.paysimplextest.data.common.module.NetworkModule
import com.example.paysimplextest.data.login.remote.api.PokemonApi
import com.example.paysimplextest.data.login.repository.PokemonRepositoryImpl
import com.example.paysimplextest.domain.login.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class PokemonModule {

    @Singleton
    @Provides
    fun providePokemonApi(retrofit: Retrofit) : PokemonApi {
        return retrofit.create(PokemonApi::class.java)
    }

    @Singleton
    @Provides
    fun providePokemonRepository(pokemonApi: PokemonApi) : PokemonRepository {
        return PokemonRepositoryImpl(pokemonApi)
    }


}