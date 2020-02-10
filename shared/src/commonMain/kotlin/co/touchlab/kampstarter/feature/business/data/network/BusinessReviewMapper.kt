package co.touchlab.kampstarter.feature.business.data.network

/**
 * @author Ryan Simon
 */
class BusinessReviewMapper {
    fun make(businessReviewsResponse: BusinessReviewsResponse) = businessReviewsResponse.reviews
}