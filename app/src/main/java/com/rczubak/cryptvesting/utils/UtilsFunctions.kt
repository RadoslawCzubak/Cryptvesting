package com.rczubak.cryptvesting.utils

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
