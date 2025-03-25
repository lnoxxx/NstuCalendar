package com.lnoxdev.data

sealed class DataException(message: String) : Exception(message)

class InternetException(message: String) : DataException(message)
class ParseException(message: String) : DataException(message)
class SaveException(message: String) : DataException(message)

class SettingGroupException(message: String) : DataException(message)