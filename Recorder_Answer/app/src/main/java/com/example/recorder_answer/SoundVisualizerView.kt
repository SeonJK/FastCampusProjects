package com.example.recorder_answer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

/**
 * Recorder_Answer
 * Created by authorName
 * Date: 2022-04-15
 * Time: 오후 9:23
 * */
class SoundVisualizerView (
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    companion object {
        private const val LINE_WIDTH = 10F
        private const val LINE_SPACE = 15F
        private const val MAX_AMPLITUDE = Short.MAX_VALUE.toFloat()   // 32767.0
        private const val ACTION_INTERVAL = 20L
    }

    // 현재 진폭을 가져오는 호출 메소드
    var onRequestCurrentAmplitude: (() -> Int)? = null

    // Paint 객체 생성
    // 이것이 선행되어야 onDraw() 메소드를 오버라이드 할 수 있음
    private val amplitudePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.purple_500)
        strokeWidth = LINE_WIDTH
        strokeCap = Paint.Cap.ROUND
    }

    private var drawingWidth: Int = 0
    private var drawingHeight: Int = 0

    // 가지고 있는 amplitude들을 모아놓은 리스트
    private var drawingAmplitudes: List<Int> = emptyList()

    private var isReplaying = false
    private var replayingPosition = 0

    // Paint를 왼쪽으로 이동시켜서 흐르는 느낌을 주는 Thread
    private val visualizeRepeatAction: Runnable = object : Runnable {
        override fun run() {
            if(!isReplaying) {
                // Amplitude
                val newAmplitude = onRequestCurrentAmplitude?.invoke() ?: 0
                drawingAmplitudes = listOf(newAmplitude) + drawingAmplitudes
            } else {
                replayingPosition++
            }

            // Draw 호출하여 화면 갱신
            invalidate()

            // 20ms마다 Thread 실행
            handler?.postDelayed(this, ACTION_INTERVAL)
        }
    }

    // Custom Drawing할 때 뷰의 크기를 아는 것은 중요하다.
    // 이 메소드를 오버라이드하여 화면모드, 크기, 밀도, 비율을 처리한다.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawingHeight = h
        drawingWidth = w

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 그릴 객체가 null이면 리턴
        canvas ?: return
        // 진폭의 높이
        val centerY = drawingHeight / 2f
        // 진동의 시작지점 == 가로길이
        var offsetX = drawingWidth.toFloat()

        // 리스트 요소 각각에 대해
        drawingAmplitudes
            .let { amplitude ->
                if(isReplaying) {
                    // 재생할 경우 기존의 것을 다시 가져옴
                    amplitude.takeLast(replayingPosition)
                } else {
                    // 녹음할 경우 현재 것을 보냄
                    amplitude
                }
            }
            .forEach { amplitude ->
            // 그리려는 길이 = 진폭의 값 / 최댓값 * 그리려고 하는 높이 * 80퍼센트
            val lineLength = amplitude / MAX_AMPLITUDE * drawingHeight * 0.8f

            // offset 계산 -> 오른쪽부터 왼쪽으로 가야하기 때문에 -1씩 감소
            offsetX -= LINE_SPACE
            // 왼쪽 바깥으로 벗어날 경우
            if(offsetX < 0) return@forEach

            // centerY를 기준으로 +와 - 선을 그려줌
            canvas.drawLine(
                offsetX,
                centerY - lineLength / 2f,
                offsetX,
                centerY + lineLength / 2f,
                amplitudePaint
            )
        }
    }

    fun startVisualizing(isReplaying: Boolean) {
        this.isReplaying = isReplaying
        handler?.post(visualizeRepeatAction)
    }

    fun stopVisualizing() {
        replayingPosition = 0
        handler?.removeCallbacks(visualizeRepeatAction)
    }

    fun resetVisualizing() {
        drawingAmplitudes = emptyList()
        invalidate()
    }
}