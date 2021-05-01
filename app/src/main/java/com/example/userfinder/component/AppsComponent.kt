package com.example.userfinder.component

import com.example.userfinder.page.MainActivity
import com.example.userfinder.MainApp
import com.example.userfinder.network.NetworkModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface AppsComponent {

    fun inject(mainApp: MainApp)
    fun inject(mainActivity: MainActivity)
}
