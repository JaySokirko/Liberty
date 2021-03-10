package com.sokyrko.internal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sokyrko.internal.AnnotationProcessor.getMethodsAnnotatedWith
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

//        permissions.forEach { permission ->
//            resultList.add(Permission(name = permission, isGranted = isHavePermission(permission)))
//        }

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

        if (permissions.count() == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                if (shouldShowRequestPermissionRationale(context!!, permission = permissions[0])) {
                    val onDenied: Method? =
                        getMethodsAnnotatedWith(receiver, OnDenied::class.java).firstOrNull {
                            it.getAnnotation(OnDenied::class.java)?.requestCode == requestCode
                        }
                    AnnotationProcessor.invokeMethod(onDenied, receiver)
                }
                else {
                    val onNeverAskAgain: Method? =
                        getMethodsAnnotatedWith(receiver, OnNeverAskAgain::class.java).firstOrNull {
                            it.getAnnotation(OnNeverAskAgain::class.java)?.requestCode == requestCode
                        }
                    AnnotationProcessor.invokeMethod(onNeverAskAgain, receiver)
                }
            }

            else if (grantResults[0] == PERMISSION_GRANTED) {
                val onAllowed: Method? =
                    getMethodsAnnotatedWith(receiver, OnAllowed::class.java).firstOrNull {
                        it.getAnnotation(OnAllowed::class.java)?.requestCode == requestCode
                    }
                AnnotationProcessor.invokeMethod(onAllowed, receiver)
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
                getMethodsAnnotatedWith(receiver, OnPermissionsRequestResult::class.java).firstOrNull {
                    it.getAnnotation(OnPermissionsRequestResult::class.java)?.requestCode == requestCode
                }

            AnnotationProcessor.invokeMethod(method, receiver, permissionsResultList)
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