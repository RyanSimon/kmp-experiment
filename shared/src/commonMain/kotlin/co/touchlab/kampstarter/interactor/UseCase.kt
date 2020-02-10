package co.touchlab.kampstarter.interactor


import co.touchlab.kampstarter.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import co.touchlab.kampstarter.functional.Either
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

    abstract suspend fun run(params: Params): Either<Failure, Type>

    @UseExperimental(ExperimentalTime::class)
    operator fun invoke(
            coroutineScope: CoroutineScope,
            params: Params,
            onResult: (Either<Failure, Type>) -> Unit = {}) {
        var measure: Duration = 0.toDuration(DurationUnit.MILLISECONDS)
        flow {
            val requestTime = measureTime {
                emit(run(params))
            }

            measure += requestTime

            //Napier.d("Request time: $requestTime ms")
        }
                .flowOn(Dispatchers.Main)
                .onStart {
//                    val delayTime = measureTime {
//                        delay(2000)
//                    }

                   // measure += delayTime

                   // Napier.d("Delay time: $delayTime ms")
                }
                .onEach {
                    onResult(it)
                }
                //.onCompletion { Napier.d("Flow done in: $measure ms") }
                .launchIn(coroutineScope)
    }

    class None
}