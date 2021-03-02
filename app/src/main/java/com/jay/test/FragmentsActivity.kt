package com.jay.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class FragmentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, PermissionsFragment())
            .commit()
    }
}