package co.touchlab.kampstarter.feature.business.data.network

interface BusinessesApi {
    /**
     * @param searchTerm
     * @param location
     * @param numResults; API defaults to 20
     * @param numResultsToSkip; API defaults to 0
     */
    suspend fun search(searchTerm: String,
               location: String,
               numResults: Int = 20,
               numResultsToSkip: Int = 0): BusinessesResponse

    suspend fun getBusinessReviews(businessId: String): BusinessReviewsResponse
}
