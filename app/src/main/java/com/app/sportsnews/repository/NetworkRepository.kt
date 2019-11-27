package com.app.sportsnews.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.app.sportsnews.api.apimodels.SearchHits
import com.app.sportsnews.api.apiservice.Api
import com.app.sportsnews.utils.Resource
import com.app.sportsnews.utils.toLiveData
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NetworkRepository @Inject constructor(val api: Api) {

    fun fetchSearchResult(query: String, page: String): LiveData<Resource<SearchHits>> {
        val result: MediatorLiveData<Resource<SearchHits>> = MediatorLiveData()
        result.postValue(Resource.loading(null))
        val source = api.getSearchResult(
            query = query,
            page = page
        )
            .subscribeOn(Schedulers.io())
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
