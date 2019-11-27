package com.app.sportsnews.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.sportsnews.R
import com.app.sportsnews.di.AppViewModelFactory
import com.app.sportsnews.utils.Status
import com.app.sportsnews.utils.showLog
import com.app.sportsnews.viewmodels.MainViewModel
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.android.synthetic.main.activity_main.*
import rx.android.schedulers.AndroidSchedulers

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
        searchView()
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

    private fun searchView() {
        RxTextView.textChanges(mTvSearch)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.toString()
            }
            .filter {
                it != "" && it.length < 3
            }
            .debounce(3, TimeUnit.SECONDS)
            .subscribe {
                viewModel.setQuery(it)
            }
    }
}
