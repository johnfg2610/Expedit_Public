package io.github.johnfg10

import org.slf4j.Logger
import java.util.*

fun Logger.exception(exception: Exception){
    this.error("Message: ${exception.message} Localized Message: ${exception.localizedMessage} \n ${exception.stackTrace}")
}

public fun ClosedRange<Int>.random() =
        Random().nextInt(endInclusive - start) +  start

fun <T> List<T>.random() : T{
    return this[(0..this.size).random()]
}

