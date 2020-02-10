package co.touchlab.kampstarter.android.feature.business

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.touchlab.kampstarter.feature.business.domain.GetBusinessesAndTopReviewsBySearch
import org.koin.core.KoinComponent
import org.koin.core.inject


/**
 * @author Ryan Simon
 */
class BusinessesViewModelFactory : ViewModelProvider.NewInstanceFactory(), KoinComponent {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val getBusinessesAndTopReviewsBySearch: GetBusinessesAndTopReviewsBySearch by inject()

        return BusinessesViewModel(getBusinessesAndTopReviewsBySearch) as T
    }
}