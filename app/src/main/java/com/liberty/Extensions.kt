package com.liberty

import android.graphics.Color
import android.widget.TextView

fun TextView.setOnAllowed() {
    text = "Allowed"
    setTextColor(Color.GREEN)
}

fun TextView.setOnDenied() {
    text = "Denied"
    setTextColor(resources.getColor(R.color.yellow))
}

fun TextView.setOnNeverAskAgain() {
    text = "Never Ask Again"
    setTextColor(Color.RED)
}