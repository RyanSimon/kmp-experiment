package co.touchlab.kampstarter.feature.business.domain

import co.touchlab.kampstarter.Failure
import co.touchlab.kampstarter.functional.Either

/**
 * @author Ryan Simon
 */
interface BusinessRepository {
    suspend fun search(searchTerm: String,
               location: String,
               numResults: Int,
               numResultsToSkip: Int): Either<Failure, List<Business>>

    suspend fun getBusinessReviews(businessId: String): Either<Failure, List<BusinessReview>>
}