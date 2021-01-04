package com.permissionstests

import android.Manifest.permission.CAMERA
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import com.permissionstests.Constants.REQUEST_CAMERA_PERMISSION
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain


class MainActivity : BaseActivity() {

    private lateinit var cameraPermissionBtn: Button
    private lateinit var cameraPermissionHintTextView: TextView

    private val TAG = "TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container, PermissionsFragment())
            .commit()

//        cameraPermissionBtn = findViewById(R.id.permission_button)
//        cameraPermissionHintTextView = findViewById(R.id.permisson_hint_text_view)
//
//        onCameraPermissionBtnClick()
    }

    private fun onCameraPermissionBtnClick() {
        cameraPermissionBtn.setOnClickListener {
            Liberty.requestPermission(CAMERA, REQUEST_CAMERA_PERMISSION)
        }
    }
}