package com.app.sportsnews.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.sportsnews.R
import com.app.sportsnews.api.apimodels.Hit
import com.app.sportsnews.databinding.ActivityMainBinding
import com.app.sportsnews.di.AppViewModelFactory
import com.app.sportsnews.ui.adapters.NewsAdapter
import com.app.sportsnews.utils.Resource
import com.app.sportsnews.utils.Status
import com.app.sportsnews.utils.showLog
import com.app.sportsnews.utils.showToast
import com.app.sportsnews.viewmodels.MainViewModel
import com.jakewharton.rxbinding.widget.RxTextView
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import rx.android.schedulers.AndroidSchedulers

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val imagesAdapter by lazy { NewsAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setViewModel()
        setSearchQuery()
        fetchData()
        searchView()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private fun fetchData() {
        viewModel.results.observe(this, Observer {
            binding.resource = it
            when (it.status) {
                Status.SUCCESS -> {
                    setRecyclerViewAndData(it.data)
                }
                Status.ERROR -> {
                    showLog("error ${it.message}")
                    showToast("Error while Fetching ${it.message}")
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
        RxTextView.textChanges(binding.mTvSearch)
            .observeOn(AndroidSchedulers.mainThread())
            .skip(1)
            .map {
                Resource.loading(null)
                it.toString()
            }
            .filter {
                !it.isNullOrEmpty()
            }
            .debounce(1, TimeUnit.SECONDS)
            .subscribe({
                viewModel.setQuery(it)
            },
                {
                    showLog("Error searchView: ${it.localizedMessage}")
                })
    }

    private fun setRecyclerViewAndData(data: List<Hit>?) {
        imagesAdapter.submitList(data)
        binding.recyclerView.adapter = imagesAdapter
    }
}
