package com.lnoxdev.data

sealed class ScheduleException(message: String) : Exception(message)
class InternetException(message: String) : ScheduleException(message)
class ParseException(message: String) : ScheduleException(message)
class SaveException(message: String) : ScheduleException(message)
class SettingGroupException(message: String) : ScheduleException(message)

sealed class GCalendarExportException(message: String): Exception(message)
class GetScheduleException(message: String): GCalendarExportException(message)
class CalendarPermissionRequired (message: String): GCalendarExportException(message)
class CalendarCreateException (message: String): GCalendarExportException(message)
class CalendarAddingEventException (message: String): GCalendarExportException(message)
class CalendarDeleteException (message: String): GCalendarExportException(message)