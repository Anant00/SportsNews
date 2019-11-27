package com.app.sportsnews.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.app.sportsnews.api.apimodels.Hit
import com.app.sportsnews.repository.NetworkRepository
import com.app.sportsnews.utils.Resource
import java.util.Locale
import javax.inject.Inject

class MainViewModel
@Inject constructor(repo: NetworkRepository) : ViewModel() {

    private val _query = MutableLiveData<String>()

    val results: LiveData<Resource<List<Hit>>> = Transformations
        .switchMap(_query) { search ->
            repo.fetchSearchResult(search, "0")
        }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == _query.value) {
            return
        }
        _query.postValue(input)
    }
}
