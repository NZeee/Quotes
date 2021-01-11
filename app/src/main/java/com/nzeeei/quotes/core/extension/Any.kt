package com.nzeeei.quotes.core.extension

fun Any.objectScopeName(): String = "${javaClass.simpleName}_${hashCode()}"
