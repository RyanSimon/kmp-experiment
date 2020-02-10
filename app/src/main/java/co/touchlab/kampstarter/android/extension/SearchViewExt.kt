package co.touchlab.kampstarter.android.extension

import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive

/**
 * @author https://github.com/LDRAlighieri/Corbind/blob/master/corbind-appcompat/src/main/kotlin/ru/ldralighieri/corbind/appcompat/SearchViewQueryTextChangeEvents.kt
 */

/**
 * Create a flow of [query text events][SearchViewQueryTextEvent] on [SearchView].
 *
 * *Note:* A value will be emitted immediately on collect.
 */
@CheckResult
fun SearchView.queryTextChangeEvents(): Flow<SearchViewQueryTextEvent> = channelFlow {
    offer(SearchViewQueryTextEvent(this@queryTextChangeEvents, query, false))
    setOnQueryTextListener(listener(this, this@queryTextChangeEvents, ::offer))
    awaitClose { setOnQueryTextListener(null) }
}

data class SearchViewQueryTextEvent(
        val view: SearchView,
        val queryText: CharSequence,
        val isSubmitted: Boolean
)

@CheckResult
private fun listener(
        scope: CoroutineScope,
        searchView: SearchView,
        emitter: (SearchViewQueryTextEvent) -> Boolean
) = object : SearchView.OnQueryTextListener {

    override fun onQueryTextChange(s: String): Boolean {
        return onEvent(SearchViewQueryTextEvent(searchView, s, false))
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return onEvent(SearchViewQueryTextEvent(searchView, query, true))
    }

    private fun onEvent(event: SearchViewQueryTextEvent): Boolean {
        if (scope.isActive) {
            emitter(event)
            return true
        }
        return false
    }
}
