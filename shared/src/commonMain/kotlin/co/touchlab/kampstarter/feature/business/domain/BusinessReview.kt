package co.touchlab.kampstarter.feature.business.domain

import kotlinx.serialization.Serializable

/**
 * @author Ryan Simon
 */
@Serializable
data class BusinessReview(val rating: Int, val text: String)
