package com.jay.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sokyrko.liberty.Liberty

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Liberty.init(this)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Liberty.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }
}