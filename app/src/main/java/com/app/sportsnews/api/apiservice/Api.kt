package com.app.sportsnews.api.apiservice

import com.app.sportsnews.api.apimodels.SearchHits
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("search")
    fun getSearchResult(
        @Query("query") query: String,
        @Query("page") page: String
    ): Flowable<List<SearchHits>>
}
