package com.example.todaywisesaying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todaywisesaying.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        // RemoteConfig 객체 생성
        val remoteConfig = Firebase.remoteConfig
        // 관련 설정 값
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60000
        }
        // 설정 값 연동
        remoteConfig.setConfigSettingsAsync(configSettings)

        // 인앱 매개변수 기본 값 설정
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Toast.makeText(this, "Fetch and Activate succeeded",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                }
                // 초기 작업
            }
    }
}