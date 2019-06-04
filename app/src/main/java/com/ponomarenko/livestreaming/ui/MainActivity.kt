package com.ponomarenko.livestreaming.ui

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_SETTLING
import com.ponomarenko.livestreaming.R
import com.ponomarenko.livestreaming.data.Video
import com.ponomarenko.livestreaming.ui.adapter.PageViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    private lateinit var adapter: PageViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val videoList = arrayListOf<Video>()
        videoList.add(Video("https://stream.livestreamfails.com/video/5cf502a04cc11.mp4".toUri()))
        videoList.add(Video("https://stream.livestreamfails.com/video/5cf4925b83d47.mp4".toUri()))
        videoList.add(Video("https://stream.livestreamfails.com/video/5acd1b84591e8.mp4".toUri()))
        videoList.add(Video("https://stream.livestreamfails.com/video/5c6f0b7879b64.mp4".toUri()))

        pager.adapter = PageViewAdapter(this, videoList, lifecycle)
    }

}
