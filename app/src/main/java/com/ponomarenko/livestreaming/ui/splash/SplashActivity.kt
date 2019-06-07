package com.ponomarenko.livestreaming.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.ponomarenko.livestreaming.R
import com.ponomarenko.livestreaming.ui.ScopedActivity
import com.ponomarenko.livestreaming.ui.ViewModelFactory
import com.ponomarenko.livestreaming.ui.main.MainActivity
import com.ponomarenko.livestreaming.ui.main.MainViewModel
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SplashActivity : ScopedActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val factory by instance<ViewModelFactory>()
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel = ViewModelProviders.of(this, factory).get(MainViewModel::class.java)


        launch {
            viewModel.movies.await().observe(this@SplashActivity, Observer {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            })
        }
    }
}