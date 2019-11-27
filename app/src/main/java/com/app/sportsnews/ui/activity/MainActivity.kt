package com.app.sportsnews.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class MainActivity : DaggerAppCompatActivity(), NewsAdapter.OnAdapterItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private val newsAdapter by lazy { NewsAdapter(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setViewModel()
        binding.recyclerView.adapter = newsAdapter
        if (savedInstanceState == null) {
            viewModel.setQuery("Baseball")
        }
        searchView()
        onScrollLoadMoreData()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.results.observe(this, Observer {
            fetchData(it, false)
        })
        viewModel.loadMoreResult.observe(this, Observer {
            fetchData(it, true)
        })
    }

    private fun fetchData(resource: Resource<List<Hit>>, isLoadMore: Boolean) {
        binding.resource = resource
        when (resource.status) {
                Status.SUCCESS -> {
                    if (isLoadMore) {
                        val list = newsAdapter.currentList.toMutableList()
                        list.addAll(resource.data as Iterable<Hit>)
                        setRecyclerViewAndData(list)
                    } else {
                        setRecyclerViewAndData(resource.data)
                    }
                }
                Status.ERROR -> {
                    showLog("error ${resource.message}")
                    showToast("Error while Fetching ${resource.message}")
                }
                Status.LOADING -> {
                    showLog("Loading")
                }
            }
    }

    private fun doSearch(v: View) {
        val query = binding.mTvSearch.text.toString()
        dismissKeyboard(v.windowToken)
        viewModel.setQuery(query)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun searchView() {
        RxTextView.textChanges(binding.mTvSearch)
            .debounce(500, TimeUnit.MILLISECONDS)
            .skip(1)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Resource.loading(null)
                it.toString()
            }
            .distinctUntilChanged()
            .filter {
                !it.isNullOrEmpty()
            }
            .subscribe({
                viewModel.setQuery(it)
            },
                {
                    showLog("Error searchView: ${it.localizedMessage}")
                })

        binding.mTvSearch.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun setRecyclerViewAndData(data: List<Hit>?) {
        newsAdapter.submitList(data)
    }

    private fun onScrollLoadMoreData() {
        var page = 0
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == newsAdapter.itemCount - 1) {
                    viewModel.incrementPage(++page)
                }
            }
        })
    }

    override fun onItemClick(position: Int) {
        val url = newsAdapter.currentList[position].url
        startActivity(Intent(this, DetailsActivity::class.java).putExtra("url", url))
        overridePendingTransition(0, 0)
    }
}
