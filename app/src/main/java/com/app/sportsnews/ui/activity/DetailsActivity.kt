package com.app.sportsnews.ui.activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.sportsnews.R
import com.app.sportsnews.databinding.DetailsActivityBinding
import com.app.sportsnews.utils.showLog

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: DetailsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity)
        loadData()
    }

    private fun loadData() {
        val url = intent.getStringExtra("url")
        showLog("Url is $url")
        binding.webView.webViewClient = WebViewClient()
        binding.webView.loadUrl(url)
    }
}
