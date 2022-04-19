package com.example.simplewebbrowser_myself

import android.app.Dialog
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.MenuItem
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.webkit.*
import android.widget.ProgressBar
import com.example.simplewebbrowser_myself.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var mOnScrollChangedListener: ViewTreeObserver.OnScrollChangedListener? = null

    private var firstPage: String = "https://www.google.com/"
    private var canGoBack: Boolean = false
    private var canGoForward: Boolean = false

    override fun onStart() {
        super.onStart()

        binding.swipeRefreshLayout.viewTreeObserver.addOnScrollChangedListener {
            binding.swipeRefreshLayout.isEnabled = binding.webView.scrollY == 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initWebView()
        pullToRefresh()

        runHomeButton()
        runBackButton()
        runForwardButton()
    }

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onStop() {
        super.onStop()
        binding.swipeRefreshLayout.viewTreeObserver.removeOnScrollChangedListener(mOnScrollChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }

    override fun onBackPressed() {
        canGoBack = binding.webView.canGoBack()
        if(canGoBack) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initWebView() {
        binding.webView.apply {
            // 내부 클래스 인스턴스화
            webViewClient = WebViewClientClass()

            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)

                    val newWebView = WebView(this@MainActivity).apply {
                        webViewClient = WebViewClient()
                        settings.javaScriptEnabled = true
                    }

                    val dialog = Dialog(this@MainActivity).apply {
                        setContentView(newWebView)
                        window!!.attributes.width = ViewGroup.LayoutParams.MATCH_PARENT
                        window!!.attributes.height = ViewGroup.LayoutParams.MATCH_PARENT
                    show()
                    }

                    newWebView.webChromeClient = object : WebChromeClient() {
                        override fun onCloseWindow(window: WebView?) {
                            super.onCloseWindow(window)
                            dialog.dismiss()
                        }
                    }

                    (resultMsg?.obj as WebView.WebViewTransport).webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }
            }
            settings.javaScriptEnabled = true   // 자바스크립트 허용여부
            settings.setSupportMultipleWindows(true)    // 새창띄우기 허용여부
            settings.javaScriptCanOpenWindowsAutomatically = true   // 자바스크립트 새창띄우기
            settings.useWideViewPort = true // 화면 사이즈 맞추기 허용여부
            settings.setSupportZoom(true)   // 화면 줌 허용여부
            settings.builtInZoomControls = true // 화면 확대 축소 허용여부
            settings.displayZoomControls = false // 화면 확대 축소 표시여부
            settings.setGeolocationEnabled(true)    // 자신의 위치 허용여부

            settings.cacheMode = WebSettings.LOAD_NO_CACHE  // 브라우저 캐시 허용 여부
            settings.domStorageEnabled = true   // 로컬저장소 허용여부
        }

        // 뒤 혹은 앞으로 갈 수 있는지 여부에 따라 버튼 활성화
        canGoBack = binding.webView.canGoBack()
        canGoForward = binding.webView.canGoForward()
//        binding.backButton.isEnabled = canGoBack
//        binding.forwardButton.isEnabled = canGoForward

        binding.webView.loadUrl(firstPage)
    }

    private fun pullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.webView.reload()
        }
    }

    private fun runHomeButton() {
        binding.homeButton.setOnClickListener {
            binding.webView.loadUrl(firstPage)
        }
    }

    private fun runBackButton() {
        binding.backButton.setOnClickListener {
            canGoBack = binding.webView.canGoBack()
            if(canGoBack) {
                binding.webView.goBack()
            }
        }
    }

    private fun runForwardButton() {
        binding.forwardButton.setOnClickListener {
            canGoForward = binding.webView.canGoForward()
            if(canGoForward) {
                binding.webView.goForward()
            }
        }
    }

    inner class WebViewClientClass : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            binding.backButton.isEnabled = canGoBack
            binding.forwardButton.isEnabled = canGoForward
            binding.loadingBar.visibility = ProgressBar.VISIBLE
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            binding.swipeRefreshLayout.isRefreshing = false
            binding.loadingBar.visibility = ProgressBar.GONE
        }
    }
}