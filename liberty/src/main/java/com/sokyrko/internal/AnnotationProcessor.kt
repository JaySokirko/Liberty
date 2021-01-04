package com.sokyrko.internal

import java.lang.reflect.Method

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
}