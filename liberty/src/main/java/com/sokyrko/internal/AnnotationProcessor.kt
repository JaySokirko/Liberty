package com.sokyrko.internal

import android.util.Log
import com.sokyrko.liberty.annotation.OnPermissionsRequestResult
import java.lang.reflect.Method
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

internal object AnnotationProcessor {

    fun getMethodsAnnotatedWith(receiver: Any, annotation: Class<out Annotation>): List<Method> {
        val methods: MutableList<Method> = mutableListOf()

        receiver.javaClass.declaredMethods.forEach { method: Method ->
            if (method.isAnnotationPresent(annotation)) {
                method.isAccessible = true
                methods.add(method)
            }
        }

        return methods
    }

    fun invokeMethod(method: Method, receiver: Any) {
        try {
            method.invoke(receiver)
        } catch (e: Exception) {
            if (e is IllegalArgumentException) {
                Log.e(TAG, "A method ${method.name} should not have any arguments.")
            } else {
                e.printStackTrace()
            }
        }
    }

    fun invokeMethod(method: Method, receiver: Any, argument: Any) {
        try {
            method.invoke(receiver, argument)
        } catch (e: Exception) {
            if (e is IllegalArgumentException) {
                Log.e(TAG, "A method ${method.name} should have only one argument with type List<Permission>.")
            } else {
                e.printStackTrace()
            }
        }
    }

    fun Method.getArguments(): List<KParameter>? = kotlinFunction?.parameters

    fun Method.getArgumentsCount(): Int = kotlinFunction?.parameters?.size ?: 0
}