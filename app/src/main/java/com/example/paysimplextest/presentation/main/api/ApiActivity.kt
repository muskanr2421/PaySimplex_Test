package com.example.paysimplextest.presentation.main.api

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paysimplextest.R
import com.example.paysimplextest.data.common.utils.WrappedResponse
import com.example.paysimplextest.data.login.remote.dto.PokemonResponse
import com.example.paysimplextest.databinding.ActivityApiBinding
import com.example.paysimplextest.domain.base.BaseActivity
import com.example.paysimplextest.domain.login.entity.PokemonEntity
import com.example.paysimplextest.domain.login.entity.Results
import com.example.paysimplextest.features.main.api.ApiViewModel
import com.example.paysimplextest.features.main.api.PokemonActivityState
import com.example.paysimplextest.presentation.common.extension.gone
import com.example.paysimplextest.presentation.common.extension.visible
import com.example.paysimplextest.presentation.main.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ApiActivity : BaseActivity<ApiViewModel, ActivityApiBinding>() {

    override fun getViewModel(): ApiViewModel = ViewModelProvider(this)[ApiViewModel::class.java]

    override val layoutRes: Int get() = R.layout.activity_api

    val resultList = ArrayList<Results>()


    override fun onReadyToRender(
        binder: ActivityApiBinding,
        mViewModel: ApiViewModel,
        savedInstanceState: Bundle?,
    ) {
        supportActionBar?.title = "Pokemon List"
        initValues()
        mViewModel.pokemon()
        observe(mViewModel)
    }
    private fun initValues() {
        mContext = this

        binder.idPBLoading.visible()

        binder.fabMap.setOnClickListener {
            startActivity(Intent(this@ApiActivity, MapActivity::class.java))
        }
    }

    private fun observe(viewModel: ApiViewModel){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: PokemonActivityState){
        when(state){
            is PokemonActivityState.Init -> Unit
            is PokemonActivityState.ErrorResponse -> handleErrorPokemon(state.rawResponse)
            is PokemonActivityState.SuccessResponse -> handleSuccessPokemon(state.pokemonEntity)
            is PokemonActivityState.ShowToast -> Log.d(TAG, "handleStateChange: ${state.message}")
            else -> {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleErrorPokemon(response: WrappedResponse<PokemonResponse>){
        Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        binder.idPBLoading.gone()
        Log.d(TAG, "handleSuccessPokemon: $response")
    }

    private fun handleSuccessPokemon(pokemonEntity: PokemonEntity){
        resultList.clear()
        pokemonEntity.results?.let { resultList.addAll(it) }

        Log.d(TAG, "handleSuccessPokemon: $resultList")
        binder.idPBLoading.gone()

        val apiAdapter = ApiAdapter(this, resultList)
        binder.recyclerView.layoutManager = LinearLayoutManager(this)
        binder.recyclerView.adapter = apiAdapter
    }

}