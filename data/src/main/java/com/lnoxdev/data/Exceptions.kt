package com.lnoxdev.data

sealed class ScheduleException(message: String) : Exception(message)
class InternetException(message: String) : ScheduleException(message)
class ParseException(message: String) : ScheduleException(message)
class SaveException(message: String) : ScheduleException(message)
class SettingGroupException(message: String) : ScheduleException(message)

sealed class GCalendarExportException(message: String): Exception(message)
class GetScheduleException(message: String): GCalendarExportException(message)