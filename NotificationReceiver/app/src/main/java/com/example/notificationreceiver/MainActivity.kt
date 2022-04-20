package com.example.notificationreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notificationreceiver.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initFirebase()
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    binding.firebaseTokenTextView.text = task.result
                }
            }
    }
}