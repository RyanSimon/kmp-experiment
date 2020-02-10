package co.touchlab.kampstarter.feature.business.data.network

import co.touchlab.kampstarter.runBlocking
import co.touchlab.stately.ensureNeverFrozen
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.headersOf
import io.ktor.http.parametersOf
import io.ktor.http.takeFrom
import kotlinx.serialization.json.Json

/**
 * @author Ryan Simon
 */
class BusinessesApiImpl(private val apiKey: String) : BusinessesApi {

    private val bearerHeaderValuePrefix = "Bearer "
    private val authHeaderKey = "Authorization"

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
        headersOf(authHeaderKey,bearerHeaderValuePrefix + apiKey)
    }

    init {
        ensureNeverFrozen()
    }

    override suspend fun search(
        searchTerm: String,
        location: String,
        numResults: Int,
        numResultsToSkip: Int
    ): BusinessesResponse {
        return client.submitForm(formParameters = parametersOf(Pair("term", listOf(searchTerm)), Pair("location", listOf(location)), Pair("limit", listOf(numResults.toString())), Pair("offset", listOf(numResultsToSkip.toString()))), encodeInQuery = true) {
            yelp("v3/businesses/search")
        }
    }

    override suspend fun getBusinessReviews(businessId: String): BusinessReviewsResponse {
        return  client.get {
            yelp("v3/businesses/$businessId/reviews")
        }
    }

    private fun HttpRequestBuilder.yelp(path: String) {
        url {
            headers.append(authHeaderKey, bearerHeaderValuePrefix + apiKey)
            takeFrom("https://api.yelp.com/")
            encodedPath = path
        }
    }
}