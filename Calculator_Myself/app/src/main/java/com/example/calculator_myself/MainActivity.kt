package com.example.calculator_myself

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.calculator_myself.databinding.ActivityMainBinding
import com.example.calculator_myself.model.History

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // database 초기화
        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "history-database"
            ).build()

        // TODO 쓰레드로 계산 구현하기
//        val runnable = Runnable {
//
//        }

        // 버튼 클릭시 넣을 텍스트 설정
        binding.btnZero.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("0") }
        binding.btnOne.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("1") }
        binding.btnTwo.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("2") }
        binding.btnThree.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("3") }
        binding.btnFour.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("4") }
        binding.btnFive.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("5") }
        binding.btnSix.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("6") }
        binding.btnSeven.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("7") }
        binding.btnEight.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("8") }
        binding.btnNine.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("9") }
        binding.btnPlus.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("+") }
        binding.btnSubtract.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("-") }
        binding.btnMultiply.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("⨯") }
        binding.btnDivision.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("÷") }
        binding.btnModulo.setOnClickListener { binding.tvMain.text = binding.tvMain.text.append("%") }

        // 클리어 버튼 설정
        binding.btnClear.setOnClickListener {
            binding.tvMain.text.clear()
            binding.tvSub.text = "" }

        // 저장 버튼 설정
        binding.btnSave.setOnClickListener {
            val newHistory = History(
                binding.tvMain.text.toString(),
                binding.tvSub.text.toString()
            )
            Log.d(TAG, "MainActivity - onCreate() called :: newHistory = $newHistory")
            db.historyDao().insert(newHistory)
        }
    }
}
