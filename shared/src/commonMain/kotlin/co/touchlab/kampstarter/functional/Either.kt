package co.touchlab.kampstarter.functional

/**
 * @author Ryan Simon
 *
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 */
sealed class Either<out L, out R> {
    data class Error<out L>(val value: L) : Either<L, Nothing>()
    data class Success<out R>(val value: R) : Either<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isError get() = this is Error<L>

    fun <L> error(value: L) = Error(value)
    fun <R> success(value: R) = Success(value)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
            when (this) {
                is Error -> fnL(value)
                is Success -> fnR(value)
            }

    suspend fun eitherSuspend(fnL: suspend (L) -> Any, fnR: suspend (R) -> Any): Any =
            when (this) {
                is Error -> fnL(value)
                is Success -> fnR(value)
            }
}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
        when (this) {
            is Either.Error -> Either.Error(value)
            is Either.Success -> fn(value)
        }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::success))