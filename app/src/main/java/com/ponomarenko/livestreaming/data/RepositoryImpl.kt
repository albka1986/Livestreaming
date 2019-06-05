package com.ponomarenko.livestreaming.data

import androidx.lifecycle.MutableLiveData
import com.ponomarenko.livestreaming.data.model.Post
import com.ponomarenko.livestreaming.data.network.FailNetworkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RepositoryImpl(private val failNetworkDataImpl: FailNetworkData) : Repository {

    private val _movies = MutableLiveData<List<Post>>()

    override suspend fun getMovies(): MutableLiveData<List<Post>> {
        parseMoviesFromLivestreamfails()
        return _movies
    }

    init {
        failNetworkDataImpl.movies.observeForever {
            _movies.postValue(it)
        }
    }

    private fun parseMoviesFromLivestreamfails() {
        GlobalScope.launch(Dispatchers.IO) {
            failNetworkDataImpl.getVideos()
        }
    }
}