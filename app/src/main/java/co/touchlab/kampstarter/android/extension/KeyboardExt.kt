package co.touchlab.kampstarter.android.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


/**
 * @author Ryan Simon
 */
fun Activity.hideKeyboard() {
    val view = this.findViewById<View>(android.R.id.content)
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}