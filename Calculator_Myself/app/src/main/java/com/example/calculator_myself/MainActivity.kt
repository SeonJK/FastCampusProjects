package com.example.calculator_myself

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calculator_myself.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}