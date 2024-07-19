package com.example.mytodolist.data.di

import android.app.Application
import com.example.mytodolist.App
import com.example.mytodolist.MainActivity
import com.example.mytodolist.data.database.AppDatabase
import com.example.mytodolist.data.domain.ITodoItemsRepository
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton


@Singleton
@Component(modules = [RepositoryModule::class, AppModule::class, ActivityModule::class, DatabaseModule::class])
interface AppComponent {
    fun getTodoItemsRepository(): ITodoItemsRepository
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun activityComponent(): ActivityComponent.Factory
}

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    fun inject(activity: MainActivity)
}