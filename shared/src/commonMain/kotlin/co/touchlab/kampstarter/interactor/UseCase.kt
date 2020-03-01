package co.touchlab.kampstarter.interactor


import co.touchlab.kampstarter.Failure
import co.touchlab.kampstarter.functional.Either
import co.touchlab.kampstarter.interactor.UseCase.MainScope
import co.touchlab.kampstarter.printThrowable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.CoroutineContext
import kotlin.time.*

/**
 * @author https://raw.githubusercontent.com/android10/Android-CleanArchitecture-Kotlin/d5b92eb3df6e007c14a3ec4f739f4b0b2e140738/app/src/main/kotlin/com/fernandocejas/sample/core/interactor/UseCase.kt
 *
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means that any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 */
abstract class UseCase<out Type, in Params> where Type : Any {

    internal val mainScope = MainScope(Dispatchers.Main)

    abstract suspend fun run(params: Params): Either<Failure, Type>

    @UseExperimental(ExperimentalTime::class)
    operator fun invoke(
        params: Params,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        var measure: Duration = 0.toDuration(DurationUnit.MILLISECONDS)
        flow {
            val requestTime = measureTime {
                emit(run(params))
            }

            measure += requestTime
        }
            .flowOn(Dispatchers.Main)
            .onEach {
                onResult(it)
            }
            .launchIn(mainScope)
    }

    open fun cancel() {
        mainScope.job.cancel()
    }

    class None

    internal class MainScope(private val mainContext: CoroutineContext) : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = mainContext + job + exceptionHandler

        internal val job = SupervisorJob()
        private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            showError(throwable)
        }

        //TODO: Some way of exposing this to the caller without trapping a reference and freezing it.
        fun showError(t: Throwable) {
            printThrowable(t)
        }
    }
}