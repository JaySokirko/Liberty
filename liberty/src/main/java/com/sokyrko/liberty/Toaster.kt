package com.sokyrko.liberty

import android.content.Context
import android.widget.Toast

object Toaster {

    fun message(c: Context?, message: String?) {
        Toast.makeText(c, message, Toast.LENGTH_SHORT).show()
    }

}