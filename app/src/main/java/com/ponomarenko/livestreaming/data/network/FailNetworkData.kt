package com.ponomarenko.livestreaming.data.network

import androidx.lifecycle.LiveData
import com.ponomarenko.livestreaming.data.model.Post

interface FailNetworkData {

    val movies : LiveData<List<Post>>

    suspend fun getVideos()
}