package com.irakliy.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.irakliy.domain.GetMoviesUseCase
import com.irakliy.domain.MovieModel
import com.irakliy.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : BaseViewModel() {

    val getMovies: LiveData<List<MovieModel>>
        get() = _getMovies

    private val _getMovies = MutableLiveData<List<MovieModel>>()

    val setProgress: LiveData<Boolean>
        get() = _setProgress

    private val _setProgress = MutableLiveData<Boolean>()

    var srcList: List<MovieModel>? = null

    fun loadMovies() {
        getMoviesUseCase()
            .doOnSubscribe { _setProgress.postValue(true) }
            .subscribeOn(Schedulers.io())
            .subscribe({
                srcList = it.toList()
                _getMovies.postValue(it)
                _setProgress.postValue(false)
            }, {
                Log.e(TAG, it.message.orEmpty())
                _setProgress.postValue(false)
            })
            .addToCompositeDisposable()
    }

    private companion object {
        const val TAG = "MainViewModel"
    }

}