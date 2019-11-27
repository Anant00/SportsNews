package com.app.sportsnews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.sportsnews.api.apimodels.Hit
import com.app.sportsnews.api.apiservice.Api
import com.app.sportsnews.utils.Resource
import com.app.sportsnews.utils.toLiveData
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkRepository @Inject constructor(val api: Api) {

    fun fetchSearchResult(query: String, page: String): LiveData<Resource<List<Hit>>> {
        val result: MediatorLiveData<Resource<List<Hit>>> = MediatorLiveData()
        result.postValue(Resource.loading(null))
        val source = api.getSearchResult(
            query = query,
            page = page
        )
            .subscribeOn(Schedulers.io())
            .flatMapIterable { it.hits }
            .filter {
                !it.title.isNullOrEmpty() && !it.url.isNullOrEmpty()
            }
            .toList()
            .toFlowable()
            .map {
                Resource.success(it)
            }
            .onErrorReturn {
                Resource.error(it.localizedMessage, null)
            }
            .toLiveData()

        result.addSource(source) {
            result.value = it
            result.removeSource(source)
        }
        return result
    }
}
