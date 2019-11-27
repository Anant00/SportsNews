package com.app.sportsnews.api.apimodels

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Author {
    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("matchLevel")
    @Expose
    var matchLevel: String? = null
    @SerializedName("matchedWords")
    @Expose
    var matchedWords: List<Any>? = null
}
