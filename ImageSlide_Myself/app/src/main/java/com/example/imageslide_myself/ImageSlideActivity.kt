package com.example.imageslide_myself

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import com.example.imageslide_myself.databinding.ActivityImageSlideBinding
import java.util.*
import kotlin.concurrent.timer

class ImageSlideActivity : AppCompatActivity() {

    private val binding: ActivityImageSlideBinding by lazy {
        ActivityImageSlideBinding.inflate(layoutInflater)
    }

    private var timer: Timer? = null

    private var currentPosition = 0

    private val imageList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 데이터 파싱 작업
        getImageUriFromIntent()
    }

    override fun onStart() {
        super.onStart()
        // 타이머 시작, 이미지 슬라이드 시작
        crossfade()
    }

    override fun onStop() {
        super.onStop()
        // 타이머 죽이기
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 타이머 죽이기
        timer?.cancel()
    }

    // 액티비티가 포커스됐을 때
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    // 전체화면 모드 설정
    private fun hideSystemUI() {
        // 상단 Action Bar 사라지게 하기
        supportActionBar?.hide()

        // Android 11(R) 대응
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 이 값이 True이면 내부적으로 SYSTEM_UI_LAYOUT_FLAG들을 살펴보고
            // 해당 설정 값들을 토대로 화면을 구성하게 된다.
            // 따라서 False로 설정을 해야 이제 Deprecated될
            // Flag 값들을 무시하고 Window Insets를 통해 화면을 구성할 수 있다.
            window.setDecorFitsSystemWindows(false)

            val controller = window.insetsController
            if (controller != null) {
                // 상태바와 네비게이션 바 없애기
                controller.hide(WindowInsets.Type.statusBars() or
                        WindowInsets.Type.navigationBars())

                // 특정 행동(화면 끝을 스와이프하는 등)을 했을 때만 시스템 바가 나타나도록 설정
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {    // R버전 이하 대응
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }

    // 리스트에 인텐트 값 저장
    private fun getImageUriFromIntent() {
        val size = intent.getIntExtra("imageListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("image$i")?.let {
                // 데이터가 널이 아닐 경우에 실행
                imageList.add(Uri.parse(it))
            }
        }
    }

    // 타이머를 통해 이미지 슬라이드
    private fun crossfade() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (imageList.size <= current + 1) 0 else current + 1

                binding.image1.apply {
                    alpha = 0f
                    setImageURI(imageList[next])
                }

                binding.image2.apply {
                    alpha = 0f
                    setImageURI(imageList[current])
                }
                binding.image2.animate()
                    .alpha(1f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }
}