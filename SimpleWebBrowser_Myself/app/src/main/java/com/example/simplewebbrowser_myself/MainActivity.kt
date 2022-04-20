package com.example.simplewebbrowser_myself

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
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

        initViews()
        pullToRefresh()

        runHomeButton()
        runBackButton()
        runForwardButton()

        editAddress()
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
        binding.swipeRefreshLayout.viewTreeObserver.removeOnScrollChangedListener(
            mOnScrollChangedListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.webView.destroy()
    }

    override fun onBackPressed() {
        canGoBack = binding.webView.canGoBack()
        if (canGoBack) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        // 로딩바 초기화
//        binding.loadingBar.hide()

        // 웹뷰 초기화
        binding.webView.apply {
            // WebViewClient 내부 클래스 인스턴스화
            webViewClient = WebViewClientClass()
            // WebChromeClient 내부 클래스 인스턴스화
            webChromeClient = WebChromeClientClass()

            // 웹뷰 세팅값 설정
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
        binding.webView.loadUrl(firstPage)

        // 뒤 혹은 앞으로 갈 수 있는지 여부에 따라 버튼 활성화
        canGoBack = binding.webView.canGoBack()
        canGoForward = binding.webView.canGoForward()
        binding.backButton.isEnabled = canGoBack
        binding.forwardButton.isEnabled = canGoForward
    }

    // pull-to-refresh 기능
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
            if (canGoBack) {
                binding.webView.goBack()
            }
        }
    }

    private fun runForwardButton() {
        binding.forwardButton.setOnClickListener {
            canGoForward = binding.webView.canGoForward()
            if (canGoForward) {
                binding.webView.goForward()
            }
        }
    }

    private fun editAddress() {
        // 사용자가 주소 입력 완료 시 주소창 변경
        binding.addressText.setOnEditorActionListener { v, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                val loadingUrl = v.text.toString()
                if(URLUtil.isNetworkUrl(loadingUrl)) {
                    // HTTP 혹은 HTTPS를 지원하는 웹페이지일 경우
                    binding.webView.loadUrl(loadingUrl)
                } else {
                    // HTTP 혹은 HTTPS를 지원하지 않는 페이지일 경우
                    binding.webView.loadUrl("http://$loadingUrl")
                }
            }

            return@setOnEditorActionListener false
        }
    }

    // WebViewClient 객체 정의
    inner class WebViewClientClass : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            binding.loadingBar.show()

            binding.addressText.clearFocus()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            binding.swipeRefreshLayout.isRefreshing = false
            binding.loadingBar.hide()

            // 주소창 변경
            binding.addressText.setText(url)

            // history가 없을 때 버튼 활성화여부
            canGoBack = binding.webView.canGoBack()
            canGoForward = binding.webView.canGoForward()
            binding.backButton.isEnabled = canGoBack
            binding.forwardButton.isEnabled = canGoForward
        }
    }

    // WebChromeClient 객체 정의
    inner class WebChromeClientClass : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            binding.loadingBar.progress = newProgress
        }
    }
}