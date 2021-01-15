package com.sokyrko.internal

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sokyrko.internal.AnnotationProcessor.getMethodsAnnotatedWith
import com.sokyrko.liberty.Permission
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.lang.reflect.Method

internal object Core {

    private var context: Any? = null
        get() {
            field ?: throw NullPointerException("call Liberty.init(activity) or " +
                    "Liberty.init(fragment) before use the library")
            return field
        }

    fun init(activity: Activity) {
        this.context = activity
    }

    fun init(fragment: Fragment) {
        this.context = fragment
    }

    fun isHavePermission(permission: String): Boolean {
        return when(context) {
            is Activity -> {
                val check: Int = ContextCompat.checkSelfPermission((context as Activity), permission)
                check == PERMISSION_GRANTED
            }
            is Fragment -> {
                val check: Int = ContextCompat.checkSelfPermission(((context as Fragment).requireContext()), permission)
                check == PERMISSION_GRANTED
            }
            else -> {
                throw IllegalArgumentException("context does not activity or fragment")
            }
        }
    }

    fun isHavePermissions(vararg permissions: String): List<Permission> {
        val resultList: MutableList<Permission> = mutableListOf()

        permissions.forEach { permission ->
            resultList.add(Permission(name = permission, isGranted = isHavePermission(permission)))
        }

        return resultList
    }

    fun requestPermission(permission: String, requestCode: Int) {
        when(context) {
            is Activity -> {
                requestPermissions(context as Activity, arrayOf(permission), requestCode)
            }
            is Fragment -> {
                (context as Fragment).requestPermissions(arrayOf(permission), requestCode)
            }
        }
    }

    fun requestPermissions(vararg permissions: String, requestCode: Int) {
        when(context) {
            is Activity -> {
                requestPermissions(context as Activity, permissions, requestCode)
            }
            is Fragment -> {
                (context as Fragment).requestPermissions(permissions, requestCode)
            }
        }
    }

    fun onRequestPermissionsResult(
        receiver: Any,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        val onDenied: Method =
            getMethodsAnnotatedWith(receiver, OnDenied::class.java).first {
                it.getAnnotation(OnDenied::class.java)?.permissionRequestCode == requestCode
            }

        val onNeverAskAgain: Method =
            getMethodsAnnotatedWith(receiver, OnNeverAskAgain::class.java).first {
                it.getAnnotation(OnNeverAskAgain::class.java)?.permissionRequestCode == requestCode
            }

        val onAllowed: Method =
            getMethodsAnnotatedWith(receiver, OnAllowed::class.java).first {
                it.getAnnotation(OnAllowed::class.java)?.permissionRequestCode == requestCode
            }

        permissions.forEachIndexed { index: Int, permission: String ->

            if (grantResults[index] == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(context!!, permission = permission)) {
                    AnnotationProcessor.handlePermissions(onDenied, permission, receiver)
                } else {
                    AnnotationProcessor.handlePermissions(onNeverAskAgain, permission, receiver)
                }
            }
            else if (grantResults[index] == PERMISSION_GRANTED) {
                AnnotationProcessor.handlePermissions(onAllowed, permission, receiver)
            }
        }
    }

    fun clear() {
        context = null
    }

    private fun shouldShowRequestPermissionRationale(context: Any, permission: String): Boolean {
        if (context is Activity) return context.shouldShowRequestPermissionRationale(permission)
        if (context is Fragment) return context.shouldShowRequestPermissionRationale(permission)
        else throw IllegalArgumentException("context does not activity or fragment")
    }
}