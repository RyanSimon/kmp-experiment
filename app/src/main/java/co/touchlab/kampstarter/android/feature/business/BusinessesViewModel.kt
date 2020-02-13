package co.touchlab.kampstarter.android.feature.business

import android.util.Log
import androidx.lifecycle.*
import co.touchlab.kampstarter.Failure
import co.touchlab.kampstarter.feature.business.domain.BusinessAndTopReview
import co.touchlab.kampstarter.feature.business.domain.GetBusinessesAndTopReviewsBySearch
import kotlin.time.ExperimentalTime

/**
 * @author Ryan Simon
 */
class BusinessesViewModel(private val getBusinessesAndTopReviewsBySearch: GetBusinessesAndTopReviewsBySearch) : ViewModel() {

    private val numResults = 20
    private var numResultsToSkip = 0
    private val location = "San Francisco, CA"

    /**
     * Observables
     */
    private val _businessesObservable = MutableLiveData<List<BusinessAndTopReview>>()
    val businessesObservable: LiveData<List<BusinessAndTopReview>>
        get() = _businessesObservable

    fun userSubmittedPaginatedSearch(searchTerm: String, newSearch: Boolean = true) {
        when {
            newSearch -> {
                numResultsToSkip = 0
                _businessesObservable.value = emptyList()
            }
            else -> numResultsToSkip += numResults
        }

        userSubmittedSearch(searchTerm, location, numResults, numResultsToSkip)
    }

    private fun userSubmittedSearch(searchTerm: String,
                                    location: String,
                                    numResults: Int,
                                    numResultsToSkip: Int) {
        getBusinessesAndTopReviewsBySearch(
            GetBusinessesAndTopReviewsBySearch.Params(
                searchTerm,
                location,
                numResults,
                numResultsToSkip
            )
        ) {
            it.either(::processFailure, ::processSuccess)
        }
    }

    private fun processSuccess(businesses: List<BusinessAndTopReview>) {
        _businessesObservable.value = mergeBusinessList(businesses)
    }

    private fun processFailure(failure: Failure) {
        Log.d("Error", failure::class.java.simpleName)
    }

    private fun mergeBusinessList(businesses: List<BusinessAndTopReview>): List<BusinessAndTopReview> {
        val mergedList = mutableListOf<BusinessAndTopReview>()
        val currentBusinesses = _businessesObservable.value

        currentBusinesses?.let {
            mergedList.addAll(it)
        }

        mergedList.addAll(businesses)

        return mergedList
    }

    override fun onCleared() {
        getBusinessesAndTopReviewsBySearch.cancel()
    }
}
