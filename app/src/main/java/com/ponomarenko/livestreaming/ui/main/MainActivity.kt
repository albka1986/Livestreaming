package com.ponomarenko.livestreaming.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.ponomarenko.livestreaming.R
import com.ponomarenko.livestreaming.data.model.Post
import com.ponomarenko.livestreaming.extensions.toast
import com.ponomarenko.livestreaming.ui.ScopedActivity
import com.ponomarenko.livestreaming.ui.ViewModelFactory
import com.ponomarenko.livestreaming.ui.adapter.PageViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance


class MainActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory: ViewModelFactory by instance()
    private lateinit var viewModel: MainViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)

        launch {
            viewModel.movies.await().observe(this@MainActivity, Observer {
                progress_bar.visibility = View.GONE

                val x = Post("rtmp://192.168.88.191/live")
                val listOf = listOf(x)
                val pageViewAdapter =
                    PageViewAdapter(this@MainActivity, listOf, lifecycle, object : PageViewAdapter.ErrorHandler {
                        override fun doOnError() {
                            this@MainActivity.toast("Some error")
                        }
                    })
                pager.adapter =
                    pageViewAdapter

                pager.offscreenPageLimit = 2

                pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        Log.d(TAG, "onPageSelected: $position")
                        super.onPageSelected(position)
                        pageViewAdapter.play(position, true)
                        pageViewAdapter.stopNeighbours(position)
                    }
                })
            })
        }
    }


}
