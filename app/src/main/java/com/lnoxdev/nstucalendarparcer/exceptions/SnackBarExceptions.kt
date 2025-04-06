package com.lnoxdev.nstucalendarparcer.exceptions

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.lnoxdev.nstucalendarparcer.models.UiExceptions

fun View.showErrorSnackBar(exception: UiExceptions) {
    Snackbar.make(this, exception.textResId, Snackbar.LENGTH_INDEFINITE).show()
}