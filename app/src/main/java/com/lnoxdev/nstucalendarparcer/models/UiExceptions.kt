package com.lnoxdev.nstucalendarparcer.models

import com.lnoxdev.nstucalendarparcer.R

enum class UiExceptions(val textResId: Int) {
    INTERNET(R.string.error_internet),
    PARSE(R.string.error_parse),
    UNKNOWN(R.string.error_unknown),
    SAVE(R.string.error_save),
    SETTING_GROUP(R.string.error_settings_group),
}