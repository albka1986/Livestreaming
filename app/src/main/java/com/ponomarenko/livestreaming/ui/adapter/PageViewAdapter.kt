package com.ponomarenko.livestreaming.ui.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.ponomarenko.livestreaming.R
import com.ponomarenko.livestreaming.data.model.Post
import kotlinx.android.synthetic.main.fragment_video.view.*


class PageViewAdapter(
    private val context: Context,
    private val list: List<Post>,
    private val lifecycle: Lifecycle,
    private val errorHandler: ErrorHandler
) :
    RecyclerView.Adapter<PageViewAdapter.MyViewHolder>() {

    private lateinit var recyclerView: RecyclerView

    companion object {
        private const val TAG = "PageViewAdapter"
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        Log.d(TAG, "onViewAttachedToWindow")
        super.onViewAttachedToWindow(holder)
//        holder.play(true)
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        Log.d(TAG, "onViewDetachedFromWindow")
        super.onViewDetachedFromWindow(holder)
        holder.play(false)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        val view =
            LayoutInflater.from(context).inflate(R.layout.fragment_video, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: $position ")
        val mediaSource = buildMediaSource(list[position].videoUrl.toUri())
        try {
            holder.player.prepare(mediaSource)
        } catch (e: Exception) {
            errorHandler.doOnError()
        }

        holder.itemView.setOnSystemUiVisibilityChangeListener {
            Log.d(TAG, "setOnSystemUiVisibilityChangeListener: visibility - $it")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, context.getString(R.string.app_name))
        )
            .createMediaSource(uri)
    }

    fun play(position: Int, needPlay: Boolean) {
        val viewHolder = recyclerView.findViewHolderForLayoutPosition(position) as MyViewHolder
        viewHolder.play(needPlay)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerView: PlayerView = itemView.player_view
        val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
            context,
            DefaultRenderersFactory(context),
            DefaultTrackSelector(),
            DefaultLoadControl()
        ).apply {
            repeatMode = REPEAT_MODE_ONE
        }

        fun play(play: Boolean) {
            player.playWhenReady = play
        }

        init {
            Log.d(TAG, "Init new holder")
            playerView.player = player
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_PAUSE) {
                        playerView.player.playWhenReady = false
                    }
                }
            })
        }
    }

    interface ErrorHandler {
        fun doOnError()
    }
}