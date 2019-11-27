package com.app.sportsnews.di

import com.app.sportsnews.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity
}
