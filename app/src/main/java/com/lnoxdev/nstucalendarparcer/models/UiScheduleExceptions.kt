package com.lnoxdev.nstucalendarparcer.models

import androidx.annotation.StringRes
import com.lnoxdev.nstucalendarparcer.R

enum class UiScheduleExceptions(@StringRes val textResId: Int) {
    INTERNET(R.string.error_internet),
    PARSE(R.string.error_parse),
    UNKNOWN(R.string.error_unknown),
    SAVE(R.string.error_save),
    SETTING_GROUP(R.string.error_settings_group),
}

enum class UiCalendarExportException(@StringRes val textResId: Int) {
    SCHEDULE(R.string.error_get_schedule),
    PERMISSION(R.string.error_calendar_permission),
    CREATE(R.string.error_create_calendar),
    ADD(R.string.error_add_event),
    DELETE(R.string.error_delete_calendar),
    UNKNOWN(R.string.error_unknown),
}