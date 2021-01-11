package com.liberty

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sokyrko.liberty.Liberty

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Liberty.init(activity = this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Liberty.onRequestPermissionsResult(this, this, requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        Liberty.clear()
    }
}