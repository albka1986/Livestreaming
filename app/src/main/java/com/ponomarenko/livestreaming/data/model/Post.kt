package com.ponomarenko.livestreaming.data.model

data class Post(
    val title: String?,
    val streamer: String?,
    val game: String?,
    val points: Int?,
    val nsfw: Boolean?,
    val videoUrl: String,
    val sourceUrl: String?
) {
    constructor(videoUrl: String) : this(null, null, null, null, null, videoUrl, null)
}