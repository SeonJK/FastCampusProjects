package com.example.imageslide_myself

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.imageslide_myself.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(binding.img11)
            add(binding.img12)
            add(binding.img13)
            add(binding.img21)
            add(binding.img22)
            add(binding.img23)
        }
    }

    // 다음 액티비티에 넘겨주어야 하기 때문에 따로 저장
    private val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // activityResultLauncher 초기화
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // 정상적인 결과코드를 반환하지 않았을 경우 예외처리
                if (result.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    // 정상적으로 이미지를 가져왔을 경우 이미지뷰에 세팅
                    val selectedImageUri: Uri? = result.data?.data

                    if( selectedImageUri != null) {

                        // 이미지뷰가 다 찼을 때
                        if(imageUriList.size == 6) {
                            Toast.makeText(this, "이미 사진이 꽉 찼습니다.", Toast.LENGTH_SHORT).show()
                            return@registerForActivityResult
                        }

                        imageUriList.add(selectedImageUri)
                        imageViewList[imageUriList.size-1].setImageURI(selectedImageUri)
                        Toast.makeText(this, "사진을 성공적으로 가져왔습니다.", Toast.LENGTH_SHORT).show()

                    } else {    // 받아온 Uri가 null일 때
                        Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        // 사진 추가 버튼
        initAddPhotoButton()

        // 전자액자 실행 버튼
        startImageSlideButton()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            2 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한이 허용되었을 때
                    openSelector()
                } else {
                    // 권한을 거부하였을 때
                    Toast.makeText(this, "권한이 허용되지 않았습니다. 버튼을 다시 눌러 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
            else -> {
                // todo 비정상적인 requestCode가 들어왔을 때 동작

            }
        }
    }

    private fun initAddPhotoButton() {
        binding.btnAddImage.setOnClickListener {
            // 권한 받아오기
            when {
                // 권한이 허용되어져 있을 때
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                -> {
                    openSelector()  // 셀렉터 시작 함수
                }

                // 권한이 허용되지 않았을 때 교육용 팝업을 띄운 후 권한 팝업을 띄움
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    openPermissionPopup()   // 교육용 팝업 기능 함수
                }

                // 권한 요청
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
                }
            }
        }
    }

    private fun openPermissionPopup() {
        // 알람 다이얼로그 생성
        AlertDialog.Builder(this)
            .setTitle("권한 알림")
            .setMessage("외부 저장소 사용을 위해 권한이 필요합니다.")
            .setPositiveButton("허용") { _,_ ->
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
            }
            .setNegativeButton("거부") { _,_ ->
//                finish()
            }
            .create()
            .show()
    }

    private fun openSelector() {
        // 문서를 읽고 쓸 수 있는 권한으로 인텐트 정의
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            // 갤러리앱에 접근
            addCategory(Intent.CATEGORY_OPENABLE)
            // 이미지 타입 파일만 필터링
            type = "image/*"
        }
        activityResultLauncher.launch(intent)
    }

    private fun startImageSlideButton() {
        binding.btnRunSlide.setOnClickListener {
            val intent = Intent(applicationContext, ImageSlideActivity::class.java)
            // 리스트 요소를 하나하나 담아주기
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("image$index", uri.toString())
            }
            // 리스트 사이즈를 넘겨주어서 몇개까지 가져와야 하는지 알려줌
            intent.putExtra("imageListSize", imageUriList.size)
            startActivity(intent)
        }
    }
}