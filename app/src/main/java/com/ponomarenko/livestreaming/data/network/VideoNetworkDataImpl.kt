package com.ponomarenko.livestreaming.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ponomarenko.livestreaming.data.model.Fail
import com.ponomarenko.livestreaming.data.model.Post
import com.ponomarenko.livestreaming.etensions.loge
import org.jsoup.Jsoup

class VideoNetworkDataImpl(private val retrofitService: RetrofitService) : FailNetworkData {

    private val _parsedPosts = MutableLiveData<List<Post>>()
    override val movies: LiveData<List<Post>>
        get() = _parsedPosts

    private val attrHref = "a[href]"

    private suspend fun getFails(): ArrayList<Fail> {
        val models = arrayListOf<Fail>()

        try {


            val response = retrofitService.getFailsWeekAsync().await()
            val html = response.body()?.string()
            val doc = Jsoup.parse(html)

            doc.select("div.post-card")?.let {
                it.forEach { card ->
                    val title = card.selectFirst("p.title").text()
                    val postId = card.selectFirst(attrHref).attr("href")
                        .split('/', ignoreCase = true).last().toLong()

                    val thumbnailUrl = card.selectFirst("img.card-img-top").attr("src")

                    val streamerName = card.selectFirst("div.stream-info > small.text-muted")
                        ?.select(attrHref)?.get(0)?.text()
                    val gameName = card.selectFirst("div.stream-info > small.text-muted")
                        ?.select(attrHref)?.get(1)?.text()

                    val isNsfw = card.selectFirst("span.oi-warning") != null

                    val pointsElement = card.selectFirst("small.text-muted > span.oi-arrow-circle-top").parent()

                    val points = pointsElement
                        .ownText().replace(Regex("[^\\d.]"), "").toInt()

                    models.add(Fail(title, streamerName, gameName, points, isNsfw, thumbnailUrl, postId))
                }
            }

        } catch (e: Exception) {
            e.localizedMessage.loge()
        }
        return models
    }

    override suspend fun getVideos() {
        val fails = getFails()

        val posts = fails.map { fail ->
            val response = retrofitService.getPostsAsync(fail.postId).await()
            if (!response.isSuccessful) return

            val doc = Jsoup.parse(response.body()?.string())
            doc?.let {
                val title = it.selectFirst("h4.post-title").text()

                val streamer = it.selectFirst("div.post-streamer-info")
                    ?.select(attrHref)?.get(0)?.text()
                val game = it.selectFirst("div.post-streamer-info")
                    ?.select(attrHref)?.get(1)?.text()

                val nsfw = it.selectFirst("div.post-stats-info > span.oi-warning") != null

                val pointsElement = it.selectFirst("small.text-muted > span.oi-arrow-circle-top").parent()

                val points = pointsElement
                    .ownText().replace(Regex("[^\\d.]"), "").toInt()

                val videoUrl = it.selectFirst("video > source").attr("src")

                val sourceUrl = it.selectFirst("div.post-stats-info > a").attr("href")


                Post(title, streamer, game, points, nsfw, videoUrl, sourceUrl)
            }
        }.filterNotNull()
        _parsedPosts.postValue(posts)


    }


}