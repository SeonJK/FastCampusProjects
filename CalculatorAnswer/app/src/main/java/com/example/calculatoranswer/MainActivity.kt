package com.example.calculatoranswer

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.room.Room
import com.example.calculatoranswer.databinding.ActivityMainBinding
import com.example.calculatoranswer.databinding.HistoryRowBinding
import com.example.calculatoranswer.model.History

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var db: AppDatabase

    private var isOperator = false
    private var hasOperator = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()

        // 숫자 버튼 설정
        binding.btnZero.setOnClickListener { numberButtonClicked("0") }
        binding.btnOne.setOnClickListener { numberButtonClicked("1") }
        binding.btnTwo.setOnClickListener { numberButtonClicked("2") }
        binding.btnThree.setOnClickListener { numberButtonClicked("3") }
        binding.btnFour.setOnClickListener { numberButtonClicked("4") }
        binding.btnFive.setOnClickListener { numberButtonClicked("5") }
        binding.btnSix.setOnClickListener { numberButtonClicked("6") }
        binding.btnSeven.setOnClickListener { numberButtonClicked("7") }
        binding.btnEight.setOnClickListener { numberButtonClicked("8") }
        binding.btnNine.setOnClickListener { numberButtonClicked("9") }
        // 연산자 버튼 설정
        binding.btnPlus.setOnClickListener { operatorButtonClicked("+") }
        binding.btnSubtract.setOnClickListener { operatorButtonClicked("-") }
        binding.btnMultiply.setOnClickListener { operatorButtonClicked("⨯") }
        binding.btnDivision.setOnClickListener { operatorButtonClicked("÷") }
        binding.btnModulo.setOnClickListener { operatorButtonClicked("%") }

        // 클리어 버튼 설정
        clearButtonClicked()
        // 저장 버튼 설정
        saveButtonClicked()

        // 히스토리 버튼 설정
        historyButtonClicked()
        // 히스토리 닫기 버튼 설정
        binding.btnCloseHistory.setOnClickListener {
            binding.historyLayout.visibility = View.GONE
        }
        // 히스토리 삭제 버튼 설정
        historyClearButtonClicked()
    }

    private fun historyButtonClicked() {
        binding.btnHistory.setOnClickListener {
            binding.historyLayout.visibility = View.VISIBLE

            // 디비를 불러오기 전에 기존에 있던 뷰를 모두 삭제
            binding.historyLinearLayout.removeAllViews()

            // 디비에서 데이터 가져와 뷰에 그리는 기능
            Thread(Runnable {
                db.historyDao().getAll().reversed().forEach {

                    runOnUiThread {
                        val historyView = HistoryRowBinding.inflate(LayoutInflater.from(this), null, false)

                        historyView.tvExpression.text = it.expression
                        historyView.tvResult.text = "= ${it.result}"
                        // 뷰에 그리기(인자는 LayoutParams여야 함
                        binding.historyLinearLayout.addView(historyView.root)
                    }
                }
            }).start()
        }
    }

    private fun historyClearButtonClicked() {
        binding.btnClearHistory.setOnClickListener {
            // 뷰에서 기록 지우기
            binding.historyLinearLayout.removeAllViews()

            // 디비에서 데이터 삭제
            Thread(Runnable {
                db.historyDao().deleteAll()
            }).start()
        }
    }

    private fun operatorButtonClicked(operator: String) {

        // 연산자가 맨 앞에서 입력될 경우 무시
        if (binding.tvExpression.text.isEmpty()) {
            return
        }

        // 연산자를 연속으로 썼을 경우 연산자를 수정하도록 함
        when {
            // 연산자를 연속으로 썼을 경우
            isOperator -> {
                val text = binding.tvExpression.text.toString()
                binding.tvExpression.text = text.dropLast(1) + operator
            }
            // 계산에서 연산자를 하나만 사용하게끔 유도하기 위해(?)
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.tvExpression.append(" $operator")
            }
        }

        // 텍스트뷰에 연산자를 초록색으로 변경
        val ssb = SpannableStringBuilder(binding.tvExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(getColor(R.color.green)),
            binding.tvExpression.text.length - 1,
            binding.tvExpression.text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvExpression.text = ssb

        isOperator = true
        hasOperator = true

    }

    private fun numberButtonClicked(number: String) {

        // 방금 전 연산자를 추가했을 경우 빈칸을 하나 삽입하여 피연산자와 구분
        if (isOperator) {
            binding.tvExpression.append(" ")
        }
        isOperator = false

        val expressionText = binding.tvExpression.text.split(" ")

        // 숫자가 15자리 이상 넘어갔을 경우 예외처리
        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last() == "0") {
            binding.tvExpression.text = number
            return
        }

        binding.tvExpression.append(number)
        // 실시간 계산결과를 텍스트뷰에 설정
        binding.tvResult.text = calculateExpression()
    }

    private fun saveButtonClicked() {
        binding.btnSave.setOnClickListener {

            val expressionTexts = binding.tvExpression.text.split(" ")

            // 연산할게 없을 경우 예외처리
            if (binding.tvExpression.text.isEmpty() || expressionTexts.size == 1) {
                return@setOnClickListener
            }

            // 연산자는 넣었지만 피연산자가 2개가 아닐 경우 예외처리
            if (expressionTexts.size != 3 && hasOperator) {
                Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 수식이 숫자-연산자-숫자로 나뉘어지지 않는 경우 예외처리
            // String.isNumber() 자체 확장 함수
            if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
                Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // db로 넣을 값 저장
            // why? 어느 쓰레드가 먼저 실행될지 모르기 때문!!
            val expressionText = binding.tvExpression.text.toString()
            val resultText = calculateExpression()

            // db에 데이터 저장
            // db에 저장할 쓰레드 생성
            Thread(Runnable {
                db.historyDao().insert(History(null, expressionText, resultText))
            }).start()

            // 계산 결과를 수식 텍스트뷰에 저장
            binding.tvResult.text = ""
            binding.tvExpression.text = resultText
            isOperator = false
            hasOperator = false
        }
    }

    private fun calculateExpression(): String {
        val expressionTexts = binding.tvExpression.text.split(" ")

        // 텍스트뷰에 연산자가 없거나 숫자-연산자-숫자로 나뉘어지지 않는 경우 예외처리
        if (hasOperator.not() || expressionTexts.size != 3) {
            return ""
        } else if (expressionTexts[0].isNumber().not() || expressionTexts[2].isNumber().not()) {
            // String.isNumber() 자체 확장 함수

            return ""
        }

        // 계산 로직
        val exp1 = expressionTexts[0].toBigInteger()
        val exp2 = expressionTexts[2].toBigInteger()
        val op = expressionTexts[1]

        return when (op) {
            "+" -> (exp1 + exp2).toString()
            "-" -> (exp1 - exp2).toString()
            "⨯" -> (exp1 * exp2).toString()
            "÷" -> (exp1 / exp2).toString()
            "%" -> (exp1 % exp2).toString()
            else -> ""
        }
    }

    private fun clearButtonClicked() {
        binding.btnClear.setOnClickListener {
            binding.tvExpression.text = ""
            binding.tvResult.text = ""
            isOperator = false
            hasOperator = false
        }
    }
}

// String의 확장함수 구현
private fun String.isNumber(): Boolean {
    return try {
        this.toBigInteger()
        true
    } catch (e: NumberFormatException) {
        false
    }
}
