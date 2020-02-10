package co.touchlab.kampstarter

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

internal actual fun printThrowable(t: Throwable) {
    t.printStackTrace()
}

actual fun <T> runBlocking(block: suspend () -> T): T {
    return runBlocking { block() }
}
