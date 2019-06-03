package com.ponomarenko.livestreaming

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        video_list.layoutManager = LinearLayoutManager(this)
        val list = arrayListOf<Video>()
        list.add(Video("https://youtu.be/3Q11d6I21PE".toUri()))
        list.add(Video("https://stream.livestreamfails.com/video/5b786c0397062.mp4".toUri()))
        list.add(Video("https://stream.livestreamfails.com/video/58c782375ab8d.mp4".toUri()))
        video_list.adapter = LiveVideoAdapter(this, list)
    }
}
