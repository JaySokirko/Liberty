package com.sokyrko.liberty

import android.app.Activity
import androidx.fragment.app.Fragment
import com.sokyrko.internal.Core


object Liberty {

    fun init(activity: Activity) {
        Core.init(activity)
    }

    fun init(fragment: Fragment) {
        Core.init(fragment)
    }

    fun isHavePermission(permission: String, result: (Boolean) -> Unit) {
        result(Core.isHavePermission(permission))
    }

    fun isHavePermissions(vararg permissions: String, result: (List<Permission>) -> Unit) {
        result(Core.isHavePermissions(*permissions))
    }

    fun requestPermission(permission: String, requestCode: Int) {
        Core.requestPermission(permission, requestCode)
    }

    fun requestPermissions(vararg permissions: String, requestCode: Int){
        Core.requestPermissions(*permissions, requestCode = requestCode)
    }

    fun onRequestPermissionsResult(
        receiver: Any,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        Core.onRequestPermissionsResult(receiver, requestCode, permissions, grantResults)
    }

    fun clear() {
        Core.clear()
    }
}