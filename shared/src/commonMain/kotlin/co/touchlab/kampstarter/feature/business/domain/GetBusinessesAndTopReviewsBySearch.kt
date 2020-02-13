package co.touchlab.kampstarter.feature.business.domain

import co.touchlab.kampstarter.Failure
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import co.touchlab.kampstarter.interactor.UseCase
import co.touchlab.kampstarter.functional.Either
import co.touchlab.kampstarter.functional.Either.Error
import co.touchlab.kampstarter.functional.Either.Success
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Ryan Simon
 */
class GetBusinessesAndTopReviewsBySearch : UseCase<List<BusinessAndTopReview>, GetBusinessesAndTopReviewsBySearch.Params>(), KoinComponent {

    // TODO: need to inject it here, because Koin doesn't seem to support injection in iOS directly yet
    private val businessesRepository: BusinessRepository by inject()

    private lateinit var deferred: Deferred<Either<Failure, List<BusinessAndTopReview>>>

    override suspend fun run(params: Params): Either<Failure, List<BusinessAndTopReview>> {
        businessesRepository.search(
                params.searchTerm,
                params.location,
                params.numResults,
                params.numResultsToSkip
        ).eitherSuspend(
                { processBusinessFailure(it) },
                { processBusinessSuccess(it) }
        )

        return deferred.await()
    }

    private suspend fun processBusinessSuccess(businesses: List<Business>) {
        deferred = coroutineScope { async { getBusinessesWithReviews(businesses) } }
    }

    private suspend fun processBusinessFailure(failure: Failure) {
        deferred = coroutineScope { async { Error(failure) } }
    }

    private suspend fun getBusinessesWithReviews(businesses: List<Business>): Either<Failure, List<BusinessAndTopReview>> {
        val flows = businesses.map { business ->
            flowOf(business)
                    .map {
                        fetchBusinessReviews(it)
                    }
                    .map { (business, businessReviews) ->
                        mapBusinessAndReviews(business, businessReviews)
                    }
        }

        val concurrentlyFetchedReviews = merge(*flows.toTypedArray()).toList()

        return getFinalListOfBusinessWithReviews(concurrentlyFetchedReviews)
    }

    private suspend fun fetchBusinessReviews(business: Business): Pair<Business, Either<Failure, List<BusinessReview>>> {
        val results = businessesRepository.getBusinessReviews(business.id)
        var businessReviews: Either<Failure, List<BusinessReview>> = Error(Failure.ServerError)
        results.either(
                { { businessReviews = Error(it) }.invoke() },
                { { businessReviews = Success(it) }.invoke() }
        )

        return Pair(business, businessReviews)
    }

    private fun mapBusinessAndReviews(business: Business, maybeBusinessReviews: Either<Failure, List<BusinessReview>>): Either<Failure, BusinessAndTopReview> {
        var businessAndTopReview: Either<Failure, BusinessAndTopReview> = Error(Failure.ServerError)
        maybeBusinessReviews.either(
                { { businessAndTopReview = Error(it) }.invoke() },
                { {
                    val topBusinessReview = it.firstOrNull()
                    if (topBusinessReview != null) {
                        businessAndTopReview = Success(BusinessAndTopReview(business, topBusinessReview))
                    }
                }.invoke() }
        )

        return businessAndTopReview
    }

    private fun getFinalListOfBusinessWithReviews(maybeBusinessAndTopReview: List<Either<Failure, BusinessAndTopReview>>)
            : Either<Failure, List<BusinessAndTopReview>> {
        val finalValues = mutableListOf<BusinessAndTopReview>()
        var failure: Failure? = null
        for (businessAndTopReview in maybeBusinessAndTopReview) {
            businessAndTopReview.either(
                    { { failure = it }.invoke() },
                    { finalValues.add(it) }
            )
        }

        return if (finalValues.isEmpty() && failure != null) {
            Error(failure!!)
        } else {
            Success(finalValues)
        }
    }

    /**
     * Concept pulled from https://github.com/Kotlin/kotlinx.coroutines/issues/1491
     *
     * NOTE: [flows].size is ideal for concurrency, but Yelp API rate limits aren't liking how often I'm hitting the API
     */
    private fun <T> merge(vararg flows: Flow<T>): Flow<T>  = flowOf(*flows).flattenMerge(concurrency = 2)

    data class Params(
            val searchTerm: String,
            val location: String,
            val numResults: Int,
            val numResultsToSkip: Int
    )

}
