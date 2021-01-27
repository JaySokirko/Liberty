package com.jay.permission

import android.content.Context
import android.widget.Toast

object Permission {

    lateinit var context: Context

    fun showToast() {
        Toast.makeText(context, "toast", Toast.LENGTH_LONG).show()
    }
}