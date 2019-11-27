package com.app.sportsnews.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.sportsnews.R
import com.app.sportsnews.databinding.DetailsActivityBinding
import com.app.sportsnews.utils.Resource
import com.app.sportsnews.utils.showLog
import kotlinx.android.synthetic.main.details_activity.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: DetailsActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity)
        loadData()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadData() {
        binding.resource = Resource.loading(null)
        val url = intent.getStringExtra("url")
        showLog("Url is $url")
        binding.webView.loadUrl(url)
        val webSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.resource = Resource.success(null)
            }
        }
    }
}
