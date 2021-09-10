package com.rczubak.cryptvesting.common

import android.content.Context
import android.util.DisplayMetrics

fun prepareCommaSeparatedQueryParameters(listOfParameters: List<String>): String {
    var commaSeparatedParams = ""
    listOfParameters.forEachIndexed { index, s ->
        if (index == 0) {
            commaSeparatedParams += s
        }
        commaSeparatedParams += ",$s"
    }
    return commaSeparatedParams
}

fun Int.toPx(context: Context) = this * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT