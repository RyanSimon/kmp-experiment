package co.touchlab.kampstarter.android.feature.business

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import co.touchlab.kampstarter.android.EndlessRecyclerViewScrollListener
import co.touchlab.kampstarter.android.R
import co.touchlab.kampstarter.android.extension.hideKeyboard
import co.touchlab.kampstarter.android.extension.queryTextChangeEvents
import co.touchlab.kampstarter.feature.business.domain.BusinessAndTopReview
import kotlinx.android.synthetic.main.activity_businesses.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * @author Ryan Simon
 */
class BusinessesActivity : AppCompatActivity() {

    private lateinit var businessesViewModel: BusinessesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_businesses)
        setSupportActionBar(toolbar)

        businessesViewModel = ViewModelProviders.of(this, BusinessesViewModelFactory())
                .get(BusinessesViewModel::class.java)
        businessesViewModel.businessesObservable.observe(this, Observer<List<BusinessAndTopReview>> {
            it?.let {
                (businessListView.adapter as BusinessesAdapter).updateBusinesses(it)
            }
        })

        setupBusinessList()
        setupSearchView()
    }

    private fun setupSearchView() {
        searchView.setIconifiedByDefault(false)
        searchView.queryTextChangeEvents()
                .onEach {
                    val query = searchView.query.toString()

                    if (it.isSubmitted) {
                        searchSubmitted(query)
                    }
                }
                .launchIn(lifecycleScope)
    }

    private fun searchSubmitted(query: String) {
        val adapter = businessListView.adapter as BusinessesAdapter
        adapter.clear()

        businessesViewModel.userSubmittedPaginatedSearch(query)

        hideKeyboard()
        hideSearchEditCursor()
    }

    private fun setupBusinessList() {
        val businessListView = businessListView
        val scrollListener = object : EndlessRecyclerViewScrollListener(businessListView.layoutManager as GridLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                businessesViewModel.userSubmittedPaginatedSearch(searchView.query.toString(), false)
            }
        }
        businessListView.setHasFixedSize(true)
        businessListView.addOnScrollListener(scrollListener)
        businessListView.adapter =
            BusinessesAdapter()
    }

    private fun hideSearchEditCursor() {
        businessListView.requestFocus()
    }
}
