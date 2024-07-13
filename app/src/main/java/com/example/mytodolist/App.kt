package com.example.mytodolist

import android.app.Application
import com.example.mytodolist.data.database.AppDatabase
import com.example.mytodolist.data.di.AppComponent
import com.example.mytodolist.data.di.DaggerAppComponent

class App : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

}