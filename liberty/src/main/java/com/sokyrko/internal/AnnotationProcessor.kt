package com.sokyrko.internal

import com.sokyrko.liberty.annotation.PermissionName
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

    fun handlePermissions(method: Method, permission: String, receiver: Any) {
        if (method.getArgumentsCount() == 1) {
            method.invoke(receiver)
        } else {
            method.getArguments()?.forEach { methodParameter: KParameter ->
                if (methodParameter.annotations.filterIsInstance<PermissionName>().isNotEmpty()) {
                    method.invoke(receiver, permission)
                }
            }
        }
    }

    fun Method.getArguments(): List<KParameter>? = kotlinFunction?.parameters

    fun Method.getArgumentsCount(): Int = kotlinFunction?.parameters?.size ?: 0
}