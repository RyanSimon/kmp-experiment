package co.touchlab.kampstarter

expect fun currentTimeMillis(): Long

internal expect fun printThrowable(t: Throwable)

expect fun <T> runBlocking(block: suspend () -> T): T
