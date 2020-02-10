package co.touchlab.kampstarter.feature.business.data.network

import co.touchlab.kampstarter.feature.business.domain.Business
import kotlinx.serialization.Serializable

/**
 * @author Ryan Simon
 */
@Serializable
data class BusinessesResponse(val businesses: List<Business>, val total: Int)
