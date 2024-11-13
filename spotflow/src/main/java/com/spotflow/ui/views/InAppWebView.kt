package com.spotflow.ui.views

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject


@Composable
fun WebViewPage(url: String, onComplete: () -> Unit) {
    val context = LocalContext.current

    val webView = remember {
        WebView(context).apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    url?.let { processUrl(Uri.parse(it), onComplete) }
                }
            }
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
        }
    }

    AndroidView(
        modifier = Modifier
            .fillMaxSize(),
        factory = { webView },
        update = {
            it.loadUrl(url)
        }
    )
}

fun processUrl(uri: Uri, onComplete: () -> Unit) {
    if (checkHasAppendedWithResponse(uri)) {
        onComplete()
    } else {
        checkHasCompletedProcessing(uri, onComplete)
    }
}

fun checkHasCompletedProcessing(uri: Uri, onComplete: () -> Unit) {
    val status = uri.getQueryParameter("status")
    val txRef = uri.getQueryParameter("tx_ref")
    if (status != null && txRef != null) {
        onComplete()
    }
}

fun checkHasAppendedWithResponse(uri: Uri): Boolean {
    val response = uri.getQueryParameter("response")
    if (response != null) {
        val json = JSONObject(response)
        val status = json.optString("status")
        val txRef = json.optString("txRef")
        return status.isNotEmpty() && txRef.isNotEmpty()
    }
    return false
}


