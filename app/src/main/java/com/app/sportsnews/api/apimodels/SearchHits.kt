package com.app.sportsnews.api.apimodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchHits {
    @SerializedName("hits")
    @Expose
    var hits: List<Hit>? = null
}
