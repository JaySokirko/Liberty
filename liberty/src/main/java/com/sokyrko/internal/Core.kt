package com.sokyrko.internal

import android.app.Activity
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.sokyrko.internal.AnnotationProcessor.getMethodsAnnotatedWith
import com.sokyrko.liberty.Permission
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import java.lang.reflect.Method

internal object Core {

    private var context: Activity? = null
        get() {
            field.checkNotNull()
            return field
        }

    fun init(context: Activity) {
        this.context = context
    }

    fun isHavePermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(context!!, permission)
        return check == PERMISSION_GRANTED
    }

    fun isHavePermissions(vararg permissions: String): List<Permission> {
        val resultList: MutableList<Permission> = mutableListOf()

        permissions.forEach { permission ->
            val check: Int = ContextCompat.checkSelfPermission(context!!, permission)
            resultList.add(Permission(name = permission, isGranted = check == PERMISSION_GRANTED))
        }

        return resultList
    }

    fun requestPermission(permission: String, requestCode: Int) {
        requestPermissions(context!!, arrayOf(permission), requestCode)
    }

    fun requestPermissions(vararg permissions: Permission) {
        permissions.forEach { permission: Permission ->
            requestPermission(permission.name, permission.requestCode)
        }
    }

    fun onRequestPermissionsResult(
        activity: Activity,
        receiver: Any,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        val onDenied: List<Method> = getMethodsAnnotatedWith(receiver, OnDenied::class.java)
        val onNeverAskAgain: List<Method> = getMethodsAnnotatedWith(receiver, OnNeverAskAgain::class.java)
        val onAllowed: List<Method> = getMethodsAnnotatedWith(receiver, OnAllowed::class.java)

        permissions.forEachIndexed { index: Int, permission ->

            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {

                if (activity.shouldShowRequestPermissionRationale(permissions[index])) {

                    onDenied.filter {
                        it.getAnnotation(OnDenied::class.java)?.permissionRequestCode == requestCode
                    }.forEach { it.invoke(receiver) }

                } else {
                    onNeverAskAgain.filter {
                        it.getAnnotation(OnNeverAskAgain::class.java)?.permissionRequestCode == requestCode
                    }.forEach { it.invoke(receiver) }
                }
            }
            else if (grantResults[index] == PERMISSION_GRANTED) {
                onAllowed.filter {
                    it.getAnnotation(OnAllowed::class.java)?.permissionRequestCode == requestCode
                }.forEach { it.invoke(receiver) }
            }
        }
    }

    fun clear() {
        context = null
    }
}