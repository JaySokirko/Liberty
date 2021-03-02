package com.sokyrko.liberty.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class OnAllowed(val requestCode: Int)
