package com.sokyrko.internal

import android.content.Context
import java.lang.NullPointerException

internal fun Context?.checkNotNull() {
    this ?: throw NullPointerException("call Liberty.init(activity) before use the library")
}
