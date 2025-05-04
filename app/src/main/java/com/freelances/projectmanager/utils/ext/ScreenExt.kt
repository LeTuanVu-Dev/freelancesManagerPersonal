package com.freelances.projectmanager.utils.ext

import android.app.Activity
import android.content.Context
import android.os.PowerManager
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast


fun Context.dpToPx(number: Number): Float {
    return number.toFloat() * (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.pxToDp(number: Number): Float {
    return number.toFloat() / (this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun Context.getWidthScreenPx(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.getHeightScreenPx(): Int {
    return resources.displayMetrics.heightPixels
}

fun Context.getWidthScreenDp(): Float {
    return this.pxToDp(resources.displayMetrics.widthPixels)
}

fun Context.getHeightScreenDp(): Float {
    return this.dpToPx(resources.displayMetrics.heightPixels)
}

fun isScreenOn(context: Context): Boolean {
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
    return powerManager?.isInteractive ?: false
}

fun dpToPx(c: Context, dipValue: Float): Int {
    val metrics = c.resources.displayMetrics

    val `val` = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics
    )


    // Round
    var res = (`val` + 0.5).toInt()


    // Ensure at least 1 pixel if val was > 0
    if (res == 0 && `val` > 0) {
        res = 1
    }

    return res
}

fun Context.clearFocus(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.requestFocus(view: View) {
    view.requestFocus()
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(messageRes: Int) {
    Toast.makeText(this, this.getString(messageRes), Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
}
