package com.lnoxdev.nstucalendarparcer.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getThemeColor(@AttrRes colorAttribute: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(colorAttribute, typedValue, true)
    return typedValue.data
}