package com.app.sportsnews.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.sportsnews.api.apimodels.Hit
import com.app.sportsnews.repository.NetworkRepository
import com.app.sportsnews.utils.Resource
import com.app.sportsnews.utils.showLog
import java.util.Locale
import javax.inject.Inject

class MainViewModel
@Inject constructor(repo: NetworkRepository) : ViewModel() {

    private val _query = MutableLiveData<String>()
    private val _page = MutableLiveData<Int>()

    init {
        _page.postValue(0)
    }
    val results: LiveData<Resource<List<Hit>>> = Transformations
        .switchMap(_query) { search ->
            repo.fetchSearchResult(search, _page.value.toString())
        }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        _query.postValue(input)
        _page.postValue(0)
    }

    val loadMoreResult = Transformations
        .switchMap(_page) { page ->
            showLog("page is: $page")
            _query.value?.let {
                repo.fetchSearchResult(it, page.toString())
            }
        }

    fun incrementPage(page: Int) {
        _page.postValue(page)
    }

    override fun onCleared() {
        super.onCleared()
        showLog("OnCleared called")
    }
}
