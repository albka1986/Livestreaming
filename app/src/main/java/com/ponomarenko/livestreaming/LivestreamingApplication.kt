package com.ponomarenko.livestreaming

import android.app.Application
import com.ponomarenko.livestreaming.data.Repository
import com.ponomarenko.livestreaming.data.RepositoryImpl
import com.ponomarenko.livestreaming.data.network.FailNetworkData
import com.ponomarenko.livestreaming.data.network.RetrofitService
import com.ponomarenko.livestreaming.data.network.VideoNetworkDataImpl
import com.ponomarenko.livestreaming.ui.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class LivestreamingApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@LivestreamingApplication))
        bind<Repository>() with singleton { RepositoryImpl(instance()) }
        bind() from singleton { RetrofitService() }
        bind<FailNetworkData>() with singleton { VideoNetworkDataImpl(instance()) }
        bind() from provider { ViewModelFactory(instance()) }
    }

}