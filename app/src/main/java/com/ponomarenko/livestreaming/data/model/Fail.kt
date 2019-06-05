package com.ponomarenko.livestreaming.data.model

data class Fail(val title: String,
                     val streamer: String?,
                     val game: String?,
                     val points: Int,
                     val nsfw: Boolean,
                     val thumbnailUrl: String,
                     val postId: Long)