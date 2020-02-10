package co.touchlab.kampstarter.feature.business.data.network

import co.touchlab.kampstarter.feature.business.domain.BusinessReview
import kotlinx.serialization.Serializable

/**
 * @author Ryan Simon
 */
@Serializable
data class BusinessReviewsResponse(val reviews: List<BusinessReview>)