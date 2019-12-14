package com.ottarson.hackerreader.ui.shared

import android.content.ComponentName
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import com.ottarson.hackerreader.R

class WebsiteOpener(
    private val context: Context
) {
    private var chromeClient: CustomTabsClient? = null
    private var chromeSession: CustomTabsSession? = null

    private var urls: List<Uri>? = null

    private var bound: Boolean = false

    private val customTabsServiceConnection: CustomTabsServiceConnection by lazy {
        object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName?,
                client: CustomTabsClient?
            ) {
                chromeClient = client
                chromeClient?.warmup(0)
                chromeSession = chromeClient?.newSession(null)
                bound = true
                urls.let {
                    chromeSession?.mayLaunchUrl(
                        urls?.first(),
                        null,
                        urls?.subList(1, urls?.lastIndex ?: 0)?.map { url ->
                            Bundle().apply {
                                putParcelable(CustomTabsService.KEY_URL, url)
                            }
                        } ?: listOf()
                    )
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                chromeClient = null
                bound = false
            }
        }
    }

    init {
        preWarmChrome()
    }

    fun setPotentialUrls(potentialUrls: List<Uri> = listOf()) {
        if (potentialUrls.isNullOrEmpty()) {
            this.urls = null
        } else {
            this.urls = potentialUrls
        }
        preWarmChrome()
    }

    fun launch(url: Uri) {
        url.let {
            val builder = CustomTabsIntent.Builder(chromeSession)
            val mode = context.resources?.configuration?.uiMode?.and(
                Configuration.UI_MODE_NIGHT_MASK)
            when (mode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    builder.setToolbarColor(
                        ContextCompat.getColor(context, R.color.colorPrimaryDark)
                    )
                }
                Configuration.UI_MODE_NIGHT_NO, Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                    builder.setToolbarColor(
                        ContextCompat.getColor(context, R.color.colorPrimary)
                    )
                }
            }
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(context, it)
        }
    }

    private fun preWarmChrome() {
        CustomTabsClient.bindCustomTabsService(
            context,
            "com.android.chrome",
            customTabsServiceConnection
        )
    }

    fun unbindService() {
        if (bound) {
            context.unbindService(customTabsServiceConnection)
            bound = false
        }
    }
}
