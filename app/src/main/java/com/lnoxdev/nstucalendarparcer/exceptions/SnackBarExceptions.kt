package com.lnoxdev.nstucalendarparcer.exceptions

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.lnoxdev.nstucalendarparcer.models.UiCalendarExportException
import com.lnoxdev.nstucalendarparcer.models.UiScheduleExceptions

fun View.showErrorSnackBar(exception: UiScheduleExceptions) {
    Snackbar.make(this, exception.textResId, Snackbar.LENGTH_SHORT).show()
}

fun View.showErrorSnackBar(exception: UiCalendarExportException) {
    Snackbar.make(this, exception.textResId, Snackbar.LENGTH_SHORT).show()
}