package com.example.secretdiaryanswer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.example.secretdiaryanswer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var changePasswordMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // numberPicker 초기화
        binding.numberPicker1.minValue = 0
        binding.numberPicker1.maxValue = 9

        binding.numberPicker2.minValue = 0
        binding.numberPicker2.maxValue = 9

        binding.numberPicker3.minValue = 0
        binding.numberPicker3.maxValue = 9

        // 오픈버튼 기능 설정
        binding.btnOpen.setOnClickListener {
            // 비밀번호 변경 모드 활성화 중 기능동작 못하게 함
            if(changePasswordMode) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // SharedPreferences(파일이름 설정, 모드 설정)
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            // numberPicker 값을 받아올 변수
            val passwordFromUser = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            // 비밀번호 검사
            if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                // 패스워드 일치
                intent = Intent(this, DiaryActivity::class.java)
                startActivity(intent)
            } else {
                // 패스워드 불일치
                showErrorAlertDialog()
            }
        }

        // 비밀번호 변경버튼 기능 설정
        binding.btnChange.setOnClickListener {
            // SharedPreferences(파일이름 설정, 모드 설정)
            val passwordPreferences = getSharedPreferences("password", Context.MODE_PRIVATE)
            // numberPicker 값을 받아올 변수
            val passwordFromUser = "${binding.numberPicker1.value}${binding.numberPicker2.value}${binding.numberPicker3.value}"

            if(changePasswordMode) {
                // 비밀번호 변경 모드

                // SharedPreference 데이터 저장
                // commit == true일 경우, 특정 시점에 실행됨,
                // commit == false일 경우, 비동기로 지속적으로 실행됨
                passwordPreferences.edit(true) {
                    putString("password", passwordFromUser)
                }

                changePasswordMode = false
                Toast.makeText(this, "비밀번호가 변경되었습니다", Toast.LENGTH_SHORT).show()
                binding.btnChange.setBackgroundColor(Color.BLACK)

            } else {
                // 비밀번호 변경 모드 활성화 - 비밀번호가 맞는지 체크

                // 비밀번호 검사
                if (passwordPreferences.getString("password", "000").equals(passwordFromUser)) {
                    // 패스워드 일치
                    changePasswordMode = true
                    Toast.makeText(this, "변경할 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                    binding.btnChange.setBackgroundColor(Color.RED)
                } else {
                    // 패스워드 불일치
                    showErrorAlertDialog()
                }
            }
        }
    }

    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this)
            .setTitle("실패")
            .setMessage("비밀번호가 잘못되었습니다.")
            .setPositiveButton("확인") { _, _ ->  }
            .create()
            .show()
    }
}