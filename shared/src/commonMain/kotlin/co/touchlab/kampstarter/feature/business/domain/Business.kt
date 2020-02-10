package co.touchlab.kampstarter.feature.business.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Ryan Simon
 */
@Serializable
data class Business(
        val id: String,
        val name: String,
        @SerialName("image_url") val imageUrl: String
)