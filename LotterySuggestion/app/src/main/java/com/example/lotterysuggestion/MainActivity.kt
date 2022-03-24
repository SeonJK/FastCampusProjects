package com.example.lotterysuggestion

import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lotterysuggestion.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.HashSet

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var didRun = false  // 자동생성 실행여부 변수
    private val pickNumberSet = hashSetOf<Int>()    // 메인 넘버셋

    // 텍스트뷰 리스트로 묶음
    private val numberTextViewList: List<TextView> by lazy {
        listOf(
            binding.tvFirst,
            binding.tvSecond,
            binding.tvThird,
            binding.tvFourth,
            binding.tvFifth,
            binding.tvSixth
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // NumberPicker 초기설정 함수 호출
        initNumberPicker()
        // 번호 추가 함수
        runAddBtn()
        // Set 초기화 함수
        runClearBtn()
        // 랜덤 함수
        runRandomBtn()

    }

    // NumberPicker 초기 설정
    private fun initNumberPicker() {
        binding.mnumberPicker.minValue = 1
        binding.mnumberPicker.maxValue = 45

        binding.mnumberPicker.wrapSelectorWheel = false
        binding.mnumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    // 번호 추가 기능
    private fun runAddBtn() {
        binding.btnAddNum.setOnClickListener {
            // 자동생성을 실행했을 경우 예외처리
            if (didRun) {
                Toast.makeText(this, "초기화 후에 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 숫자를 6개 다 뽑은 경우 예외처리
            if(pickNumberSet.size >= 6) {
                Toast.makeText(this, "번호는 6개까지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 중복된 번호 예외처리
            if (pickNumberSet.contains(binding.mnumberPicker.value)) {
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // 텍스트뷰에 선택한 숫자 추가
            val textView = numberTextViewList[pickNumberSet.size]
            textView.text = binding.mnumberPicker.value.toString()

            // 숫자별 배경색 지정
            setNumberBackground(binding.mnumberPicker.value, textView)

            pickNumberSet.add(binding.mnumberPicker.value)
        }
    }

    private fun setNumberBackground(number: Int, textView: TextView) {
        // 숫자별 배경색 지정
        // 방법 1.
            when(number) {
                in 1..10 -> textView.setBackgroundResource(R.drawable.circle_yellow)
                in 11..20 -> textView.setBackgroundResource(R.drawable.circle_blue)
                in 21..30 -> textView.setBackgroundResource(R.drawable.circle_red)
                in 31..40 -> textView.setBackgroundResource(R.drawable.circle_gray)
                else -> textView.setBackgroundResource(R.drawable.circle_green)
            }
        // 방법 2.
        when(number) {
            in 1..10 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)
            in 21..30 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textView.background = ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }

    // 초기화 기능
    private fun runClearBtn() {
        binding.btnClear.setOnClickListener {
            // 내 방식
            // 1. mutableSet으로 했을 경우
//            pickNumberSet = emptySet<Int>() as MutableSet<Int>

            // 2. hashSet 대응
            pickNumberSet.clear()

            // 텍스트 뷰 클리어
            // 방법 1.
//            for (textView in numberTextViewList) {
//                textView.text = ""
//            }
            // 방법 2.
            numberTextViewList.forEach {
                it.text = ""
            }
            didRun = false
            //
        }
    }

    // 자동생성 버튼 리스너
    private fun runRandomBtn() {
        binding.btnAuto.setOnClickListener {
            // 랜덤 숫자 리스트 저장
            val list = getRandomNumber()

            // list 값을 텍스트뷰와 넘버셋에 저장
            // 방법 1.
//            for (i in pickNumberSet.size..5) {
//                val textView = numberTextViewList[i]
//                textView.text = list[i].toString()
//                pickNumberSet.add(list[i])
//            }
            // 방법 2.
            list.forEachIndexed { idx, number ->
                val textView = numberTextViewList[idx]
                textView.text = number.toString()
                setNumberBackground(number, textView)
            }
            didRun = true
        }
    }

    // 랜덤 리스트 리턴 기능능
    private fun getRandomNumber(): List<Int> {
        // 방법 1.
        // Set 컬렉션에 랜덤한 수를 저장
//        var lottoSet = mutableSetOf<Int>()
//        val random = Random()
//        var randomNumber: Int = 0
//
//        while(lottoSet.size < 6) {
//            randomNumber = random.nextInt(45) + 1
//            lottoSet.add(randomNumber)
//        }
        // 방법 2.
        // 1..45의 숫자가 있는 List를 다 섞고 앞의 6개 숫자만 가져가는 방식
        val lottoList = mutableListOf<Int>().apply {
            for (i in 1..45) {
                // 이미 선택한 숫자는 제외한 리스트 생성
                if (pickNumberSet.contains(i)) {
                    continue
                }
                this.add(i)
            }
        }

        // 숫자 섞기
        lottoList.shuffle()

        // 0번째부터 6개 숫자를 잘라서 newList에 저장
        // 만약 pickNumberSet에 데이터가 있으면 해당 데이터 뒤에 필요한 개수만큼 붙여서 리스트 구성
        val newList = pickNumberSet.toList() + lottoList.subList(0, 6 - pickNumberSet.size)

        // 오름차순 정렬하여 리턴
        return newList.sorted()
    }
}


