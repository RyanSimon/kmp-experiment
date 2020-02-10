package co.touchlab.kampstarter.feature.business.data

import co.touchlab.kampstarter.Failure
import co.touchlab.kampstarter.InternetConnectionHandler
import co.touchlab.kampstarter.feature.business.data.network.BusinessMapper
import co.touchlab.kampstarter.feature.business.data.network.BusinessesApi
import co.touchlab.kampstarter.feature.business.domain.Business
import co.touchlab.kampstarter.feature.business.domain.BusinessFailure.*
import co.touchlab.kampstarter.feature.business.domain.BusinessRepository
import co.touchlab.kampstarter.functional.Either
import co.touchlab.kampstarter.functional.Either.Success
import co.touchlab.kampstarter.functional.Either.Error
import co.touchlab.kampstarter.feature.business.data.network.BusinessReviewMapper
import co.touchlab.kampstarter.feature.business.domain.BusinessReview

/**
 * @author Ryan Simon
 */
class BusinessRepositoryImpl(
    private val businessesApi: BusinessesApi,
    private val internetConnectionHandler: InternetConnectionHandler,
    private val businessMapper: BusinessMapper,
    private val businessReviewMapper: BusinessReviewMapper
) : BusinessRepository {

    override suspend fun search(searchTerm: String,
               location: String,
               numResults: Int,
               numResultsToSkip: Int): Either<Failure, List<Business>> {
        return when (internetConnectionHandler.isConnected()) {
            true -> {
                try {
                    validateSearchParameters(numResults, numResultsToSkip)
                    request(businessesApi.search(searchTerm, location, numResults, numResultsToSkip)) {
                        businessMapper.make(it)
                    }
                } catch (error: IllegalArgumentException) {
                    Error(SearchParametersAreInvalid)
                }
            }
            false -> Error(Failure.NoNetworkConnection)
        }
    }

    override suspend fun getBusinessReviews(businessId: String): Either<Failure, List<BusinessReview>> {
        return when (internetConnectionHandler.isConnected()) {
            true -> {
                request(businessesApi.getBusinessReviews(businessId)) {
                    businessReviewMapper.make(it)
                }
            }
            false -> Error(Failure.NoNetworkConnection)
        }
    }

    private fun validateSearchParameters(numResults: Int, numResultsToSkip: Int) {
        require(numResults in 1..50 && numResults + numResultsToSkip <= 1000)
    }

    private fun <T, R> request(response: T, transform: (T) -> R): Either<Failure, R> {
        return try {
            Success(transform(response))
        } catch (exception: Throwable) {
            Error(Failure.ServerError)
        }
    }
}