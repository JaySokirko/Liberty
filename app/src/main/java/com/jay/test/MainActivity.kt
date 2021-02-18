package com.jay.test

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.Permission
import com.sokyrko.liberty.RequestResult
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnPermissionsRequestResult

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Liberty.init(this)

        findViewById<Button>(R.id.button).setOnClickListener {
            Liberty.requestPermissions(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CAMERA,
                requestCode = REQUEST_READ_CONTACTS_AND_CAMERA)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Liberty.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @OnDenied(REQUEST_READ_CONTACTS_CODE)
    fun onContactsRequestResult() {
        Log.d(TAG, "onContactsRequestResult:  ")
    }

    @OnPermissionsRequestResult(REQUEST_READ_CONTACTS_AND_CAMERA)
    fun onContactsAndCamera(result: MutableList<Permission>) {
        Log.d(TAG, "onContactsAndCameraAllowed: ")
        result.forEach {
            when(it.result) {
                RequestResult.DENIED -> {
                    Log.d(TAG, "DENIED: " + it.name)
                }
                RequestResult.ALLOWED -> {
                    Log.d(TAG, "ALLOWED: " + it.name)
                }
                RequestResult.NEVER_ASK_AGAIN -> {
                    Log.d(TAG, "NEVER_ASK_AGAIN: " + it.name)
                }
            }
        }
    }

    companion object {
        const val TAG = "TAG"
        const val REQUEST_READ_CONTACTS_CODE = 100
        const val REQUEST_READ_CONTACTS_AND_CAMERA = 200
    }
}