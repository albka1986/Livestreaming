package com.ponomarenko.livestreaming.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ponomarenko.livestreaming.data.Repository
import com.ponomarenko.livestreaming.data.model.Post
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class MainViewModel(private val repository: Repository) : ViewModel() {


    val movies: Deferred<LiveData<List<Post>>> = GlobalScope.async {
        repository.getMovies()
    }
}