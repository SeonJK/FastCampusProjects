package com.example.secretdiary

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.secretdiary.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isChangeBtnClicked = false

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var passwordFromUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNumberPicker()

        runOpenBtn()

        runChangeBtn()

    }

    private fun initNumberPicker() {
        binding.numberPicker1.minValue = 0
        binding.numberPicker1.maxValue = 9
        binding.numberPicker1.value = 0
        binding.numberPicker1.wrapSelectorWheel = false
        binding.numberPicker1.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.numberPicker2.minValue = 0
        binding.numberPicker2.maxValue = 9
        binding.numberPicker2.value = 0
        binding.numberPicker2.wrapSelectorWheel = false
        binding.numberPicker2.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        binding.numberPicker3.minValue = 0
        binding.numberPicker3.maxValue = 9
        binding.numberPicker3.value = 0
        binding.numberPicker3.wrapSelectorWheel = false
        binding.numberPicker3.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
    }

    private fun runChangeBtn() {
        binding.btnChange.setOnClickListener {
            if( isChangeBtnClicked ) {
                // 번호 저장
                getPassword()

                App.prefs.diaryPassword = passwordFromUser
//                App.prefs.diaryPassword.apply {
//                    App.prefs.
//                }

                isChangeBtnClicked = false
                Toast.makeText(this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show()
                binding.btnChange.setBackgroundColor(Color.BLACK)
            } else {
                // changeMode 활성화
                isChangeBtnClicked = true
                Toast.makeText(this, "변경할 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.btnChange.setBackgroundColor(Color.RED)
            }
        }
    }

    private fun runOpenBtn() {
        binding.btnOpen.setOnClickListener {
            if(isChangeBtnClicked) {
                Toast.makeText(this, "비밀번호 변경 중입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            getPassword()

            if(App.prefs.diaryPassword.equals(passwordFromUser)) {
                // 패스워드 일치
                val intent = Intent(this, textActivity::class.java)
                startActivity(intent)
            } else {
                // 패스워드 불일치
                AlertDialog.Builder(this)
                    .setTitle("실패")
                    .setMessage("비밀번호가 잘못되었습니다.")
                    .setPositiveButton("닫기") { _, _ -> }
                    .create()
                    .show()
            }
        }



    }

    private fun getPassword() {
        passwordFromUser = "${numberPicker1.value}${numberPicker2.value}${numberPicker3.value}"

    }
}