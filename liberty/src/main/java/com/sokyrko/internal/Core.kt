package com.sokyrko.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import com.sokyrko.liberty.Liberty.Permission
import com.sokyrko.liberty.Liberty.RequestResult
import com.sokyrko.liberty.annotation.OnAllowed
import com.sokyrko.liberty.annotation.OnDenied
import com.sokyrko.liberty.annotation.OnNeverAskAgain
import com.sokyrko.liberty.annotation.OnPermissionsRequestResult
import java.lang.reflect.Method

internal object Core {

    private val lifecycleObserver = LifecycleObserver()

    private var context: Any? = null
        get() {
            field ?: throw NullPointerException("call Liberty.init(activity) or " +
                    "Liberty.init(fragment) before use the library")
            return field
        }

    @SuppressLint("RestrictedApi")
    fun init(activity: Activity) {
        this.context = activity

        if (activity is ComponentActivity) {
            activity.lifecycle.addObserver(lifecycleObserver)
        } else {
            Log.e(TAG, "Looks like your activity ${activity.localClassName} inherits " +
                    "from ${Activity::class.java} instead of " +
                    "${AppCompatActivity::class.java}. To avoid memory leaks you should call " +
                    "Liberty.clear() in the onDestroy() method or extend your activity from " +
                    "${AppCompatActivity::class.java} instead of ${Activity::class.java}")
        }
    }

    fun init(fragment: Fragment) {
        this.context = fragment
        fragment.lifecycle.addObserver(lifecycleObserver)
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

        if (permissions.count() == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(context!!, permission = permissions[0])) {
                   onDenied(receiver, requestCode)
                }
                else {
                    onNeverAskAgain(receiver, requestCode)
                }
            }

            else if (grantResults[0] == PERMISSION_GRANTED) {
                onAllowed(receiver, requestCode)
            }

        } else {
            val permissionsResultList: MutableList<Permission> = mutableListOf()

            permissions.forEachIndexed { index: Int, permission: String ->
                if (grantResults[index] == PackageManager.PERMISSION_DENIED) {

                    if (shouldShowRequestPermissionRationale(context!!, permission = permissions[index])) {
                        permissionsResultList.add(Permission(permission, RequestResult.DENIED))
                    }
                    else {
                        permissionsResultList.add(Permission(permission, RequestResult.NEVER_ASK_AGAIN))
                    }
                }
                else if (grantResults[index] == PERMISSION_GRANTED) {
                    permissionsResultList.add(Permission(permission, RequestResult.ALLOWED))
                }
            }

            val method: Method? =
                AnnotationProcessor.getMethodsAnnotatedWith(receiver, OnPermissionsRequestResult::class.java)
                    .firstOrNull {
                    it.getAnnotation(OnPermissionsRequestResult::class.java)?.requestCode == requestCode
                }

            if (method != null) AnnotationProcessor.invokeMethod(method, receiver, permissionsResultList)
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

    private fun onAllowed(receiver: Any, requestCode: Int) {
        val onAllowed: Method? =
            AnnotationProcessor.getMethodsAnnotatedWith(receiver, OnAllowed::class.java)
                .firstOrNull {
                    it.getAnnotation(OnAllowed::class.java)?.requestCode == requestCode
                }

        if (onAllowed != null) AnnotationProcessor.invokeMethod(onAllowed, receiver)
    }

    private fun onDenied(receiver: Any, requestCode: Int) {
        val onDenied: Method? =
            AnnotationProcessor.getMethodsAnnotatedWith(receiver, OnDenied::class.java)
                .firstOrNull {
                    it.getAnnotation(OnDenied::class.java)?.requestCode == requestCode
                }

        if (onDenied != null) AnnotationProcessor.invokeMethod(onDenied, receiver)
    }

    private fun onNeverAskAgain(receiver: Any, requestCode: Int) {
        val onNeverAskAgain: Method? =
            AnnotationProcessor.getMethodsAnnotatedWith(receiver, OnNeverAskAgain::class.java)
                .firstOrNull {
                    it.getAnnotation(OnNeverAskAgain::class.java)?.requestCode == requestCode
                }

        if (onNeverAskAgain != null) AnnotationProcessor.invokeMethod(onNeverAskAgain, receiver)
    }
}