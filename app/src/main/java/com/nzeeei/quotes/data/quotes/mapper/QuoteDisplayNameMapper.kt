package com.nzeeei.quotes.data.quotes.mapper

import com.nzeeei.quotes.core.Mapper
import javax.inject.Inject

class QuoteDisplayNameMapper @Inject constructor() : Mapper<String, String> {
    override fun map(from: String): String = from.run {
        val part1 = subSequence(0, length / 2)
        val part2 = subSequence(part1.length, length)
        "$part1/$part2"
    }
}
