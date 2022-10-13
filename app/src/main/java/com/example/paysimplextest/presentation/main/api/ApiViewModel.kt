package com.example.paysimplextest.features.main.api

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import com.example.paysimplextest.domain.base.BaseViewModel
import com.example.paysimplextest.domain.common.base.BaseResult
import com.example.paysimplextest.domain.login.entity.PokemonEntity
import com.example.paysimplextest.domain.login.usecase.PokemonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val pokemonUseCase: PokemonUseCase): BaseViewModel() {
    private val state = MutableStateFlow<PokemonActivityState>(PokemonActivityState.Init)
    val mState: StateFlow<PokemonActivityState> get() = state

    private fun showToast(message: String){
        state.value = PokemonActivityState.ShowToast(message)
    }

    fun pokemon(){
        viewModelScope.launch {
            pokemonUseCase.execute()
                .onStart {
                }
                .catch { exception ->
                    showToast(exception.message.toString())
                    Log.d(TAG, "pokemon: ${exception.message.toString()}")
                }
                .collect { baseResult ->
                    when(baseResult){
                        is BaseResult.Error -> state.value = PokemonActivityState.ErrorResponse(baseResult.rawResponse)
                        is BaseResult.Success -> state.value = PokemonActivityState.SuccessResponse(baseResult.data)
                    }
                }
        }
    }

}

sealed class PokemonActivityState  {
    object Init : PokemonActivityState()
    data class ShowToast(val message: String) : PokemonActivityState()
    data class SuccessResponse(val pokemonEntity: PokemonEntity) : PokemonActivityState()
    data class ErrorResponse(val rawResponse: WrappedResponse<PokemonResponse>) : PokemonActivityState()
}