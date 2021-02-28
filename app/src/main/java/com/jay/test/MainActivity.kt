package com.jay.test

import android.Manifest.permission.*
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sokyrko.liberty.Liberty
import com.sokyrko.liberty.Permission
import com.sokyrko.liberty.RequestResult
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import com.sokyrko.liberty.annotation.OnPermissionsRequestResult

class MainActivity : AppCompatActivity() {

    private lateinit var requestReadContactsBtn: Button
    private lateinit var readContactsResultTextView: TextView
    private lateinit var requestScopePermissionsBtn: Button
    private lateinit var cameraResultTextView: TextView
    private lateinit var storageResultTextView: TextView
    private lateinit var smsResultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Liberty.init(activity = this)

        requestReadContactsBtn = findViewById(R.id.single_permission_request_btn)
        readContactsResultTextView = findViewById(R.id.single_permission_request_text_view)
        requestScopePermissionsBtn = findViewById(R.id.scope_permissions_request_btn)
        cameraResultTextView = findViewById(R.id.camera_result_text_view)
        storageResultTextView = findViewById(R.id.external_storage_result_text_view)
        smsResultTextView = findViewById(R.id.read_sms_result_text_view)

        requestReadContactsBtn.setOnClickListener {
            Liberty.requestPermission(READ_CONTACTS, REQUEST_READ_CONTACTS)
        }

        requestScopePermissionsBtn.setOnClickListener {
            Liberty.requestPermissions(
                CAMERA,
                READ_EXTERNAL_STORAGE,
                READ_SMS,
                requestCode = REQUEST_SCOPE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Liberty.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    @OnAllowed(REQUEST_READ_CONTACTS)
    fun onContactsRequestAllowed() {
        readContactsResultTextView.text = "Result: Allowed"
        readContactsResultTextView.setTextColor(Color.GREEN)
    }

    @OnDenied(REQUEST_READ_CONTACTS)
    fun onContactsRequestDenied() {
        readContactsResultTextView.text = "Result: Denied"
        readContactsResultTextView.setTextColor(getColor(R.color.yellow))
    }

    @OnNeverAskAgain(REQUEST_READ_CONTACTS)
    fun onContactsRequestNeverAskAgain() {
        readContactsResultTextView.text = "Result: Never ask again"
        readContactsResultTextView.setTextColor(Color.RED)
    }

    @OnPermissionsRequestResult(REQUEST_SCOPE_PERMISSIONS)
    fun onContactsAndCamera(result: MutableList<Permission>) {
        result.forEach { permission: Permission ->

            when(permission.result) {

                RequestResult.DENIED -> {
                    when(permission.name) {
                        CAMERA -> {
                            cameraResultTextView.text = "Camera: Denied"
                            cameraResultTextView.setTextColor(getColor(R.color.yellow))
                        }
                        READ_EXTERNAL_STORAGE -> {
                            storageResultTextView.text = "Storage: Denied"
                            storageResultTextView.setTextColor(getColor(R.color.yellow))
                        }
                        READ_SMS -> {
                            smsResultTextView.text = "SMS: Denied"
                            smsResultTextView.setTextColor(getColor(R.color.yellow))
                        }
                    }
                }

                RequestResult.ALLOWED -> {
                    when(permission.name) {
                        CAMERA -> {
                            cameraResultTextView.text = "Camera: Allowed"
                            cameraResultTextView.setTextColor(Color.GREEN)
                        }
                        READ_EXTERNAL_STORAGE -> {
                            storageResultTextView.text = "Storage: Allowed"
                            storageResultTextView.setTextColor(Color.GREEN)
                        }
                        READ_SMS -> {
                            smsResultTextView.text = "SMS: Allowed"
                            smsResultTextView.setTextColor(Color.GREEN)
                        }
                    }
                }

                RequestResult.NEVER_ASK_AGAIN -> {
                    when(permission.name) {
                        CAMERA -> {
                            cameraResultTextView.text = "Camera: Never ask again"
                            cameraResultTextView.setTextColor(Color.RED)
                        }
                        READ_EXTERNAL_STORAGE -> {
                            storageResultTextView.text = "Storage: Never ask again"
                            storageResultTextView.setTextColor(Color.RED)
                        }
                        READ_SMS -> {
                            smsResultTextView.text = "SMS: Never ask again"
                            smsResultTextView.setTextColor(Color.RED)
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "TAG"
        const val REQUEST_READ_CONTACTS = 100
        const val REQUEST_SCOPE_PERMISSIONS = 200
    }
}