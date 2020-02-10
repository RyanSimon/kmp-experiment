package co.touchlab.kampstarter.feature.business.data.network

/**
 * @author Ryan Simon
 */
class BusinessMapper {
    fun make(businessesResponse: BusinessesResponse) = businessesResponse.businesses
}