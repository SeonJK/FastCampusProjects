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

    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // database 초기화
        db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "history-database"
            ).build()

        // TODO 쓰레드로 계산 구현하기
//        val runnable = Runnable {
//
//        }

        // 버튼 클릭시 넣을 텍스트 설정
        binding.btnZero.setOnClickListener { binding.tvMain.append("0") }
        binding.btnOne.setOnClickListener { binding.tvMain.append("1") }
        binding.btnTwo.setOnClickListener { binding.tvMain.append("2") }
        binding.btnThree.setOnClickListener { binding.tvMain.append("3") }
        binding.btnFour.setOnClickListener { binding.tvMain.append("4") }
        binding.btnFive.setOnClickListener { binding.tvMain.append("5") }
        binding.btnSix.setOnClickListener { binding.tvMain.append("6") }
        binding.btnSeven.setOnClickListener { binding.tvMain.append("7") }
        binding.btnEight.setOnClickListener { binding.tvMain.append("8") }
        binding.btnNine.setOnClickListener { binding.tvMain.append("9") }
        binding.btnPlus.setOnClickListener { binding.tvMain.append("+") }
        binding.btnSubtract.setOnClickListener { binding.tvMain.append("-") }
        binding.btnMultiply.setOnClickListener { binding.tvMain.append("⨯") }
        binding.btnDivision.setOnClickListener { binding.tvMain.append("÷") }
        binding.btnModulo.setOnClickListener { binding.tvMain.append("%") }

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
