package com.sokyrko.liberty

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.sokyrko.internal.AnnotationProcessor
import com.sokyrko.internal.AnnotationProcessor.getMethodsAnnotatedWith
import com.sokyrko.internal.Core
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import java.lang.reflect.Method


object Liberty {

    fun init(activity: Activity) {
        Core.init(activity)
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

    fun requestPermissions(vararg permissions: Permission){
        Core.requestPermissions(*permissions)
    }

    fun onRequestPermissionsResult(
        activity: Activity,
        receiver: Any,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        Core.onRequestPermissionsResult(activity, receiver, requestCode, permissions, grantResults)
    }

    fun permission(name: String, requestCode: Int): Permission {
        return Permission(name = name, requestCode =  requestCode)
    }

    fun clear() {
        Core.clear()
    }
}