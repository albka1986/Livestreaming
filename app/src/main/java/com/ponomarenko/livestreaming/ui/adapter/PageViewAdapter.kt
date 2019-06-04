package com.ponomarenko.livestreaming.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.REPEAT_MODE_ONE
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.ponomarenko.livestreaming.data.Video
import kotlinx.android.synthetic.main.fragment_video.view.*
import java.util.*


class PageViewAdapter(private val context: Context, private val list: ArrayList<Video>) :
    RecyclerView.Adapter<PageViewAdapter.MyViewHolder>() {


    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.play(true)
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.play(false)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(com.ponomarenko.livestreaming.R.layout.fragment_video, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val mediaSource = buildMediaSource(list[position].uri)
        holder.player.prepare(mediaSource)

        holder.playerView.setOnClickListener {
            holder.playOrResume()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        return ProgressiveMediaSource.Factory(
            DefaultDataSourceFactory(context, context.getString(com.ponomarenko.livestreaming.R.string.app_name))
        )
            .createMediaSource(uri)
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
            addListener(object : Player.EventListener {


                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    when (playbackState) {
                        Player.STATE_BUFFERING -> itemView.progress_bar.visibility = View.VISIBLE
                        Player.STATE_READY -> {
                            itemView.progress_bar.visibility = View.GONE
                            if (!playWhenReady) {
                                itemView.play_btn.visibility = View.VISIBLE
                            } else {
                                itemView.play_btn.visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }


        fun playOrResume() {
            player.playWhenReady = !player.playWhenReady
        }

        fun play(play: Boolean) {
            player.playWhenReady = play
        }


        init {
            playerView.player = player
        }
    }
}