package com.nzeeei.quotes.core

interface Mapper<FROM, TO> {
    fun map(from: FROM): TO
}
