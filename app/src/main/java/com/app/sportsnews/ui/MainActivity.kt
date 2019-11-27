package com.app.sportsnews.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.sportsnews.R
import com.app.sportsnews.di.AppViewModelFactory
import com.app.sportsnews.utils.Status
import com.app.sportsnews.utils.showLog
import com.app.sportsnews.viewmodels.MainViewModel
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewModel()
        setSearchQuery()
        setData()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun setData() {
        viewModel.results.observe(this, Observer {

            when (it.status) {
                Status.SUCCESS -> {
                    showLog("success, ${it.data?.hits?.size}")
                }
                Status.ERROR -> {
                    showLog("error ${it.message}")
                }
                Status.LOADING -> {
                    showLog("Loading")
                }
            }
        })
    }

    private fun setSearchQuery() {
        viewModel.setQuery("baseball")
    }
}
