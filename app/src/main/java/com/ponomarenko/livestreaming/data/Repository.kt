package com.ponomarenko.livestreaming.data

import androidx.lifecycle.LiveData
import com.ponomarenko.livestreaming.data.model.Post

interface Repository {

    suspend fun getMovies(): LiveData<List<Post>>
}