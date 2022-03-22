package com.example.bmicalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bmicalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            // 작성없이 결과 확인을 하려할 때 오류 잡기
            try {
                // 인텐트 생성
                val intent = Intent(this, ResultActivity::class.java)
                // 인텐트 데이터 담기
                intent.putExtra("height", binding.editHeight.text.toString().toInt())
                intent.putExtra("weight", binding.editWeight.text.toString().toInt())
                // 액티비티 이동
                startActivity(intent)
            } catch (e: NumberFormatException) {
                // 에러가 날 때 토스트 메시지 호출
                Toast.makeText(this, "값이 작성되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}