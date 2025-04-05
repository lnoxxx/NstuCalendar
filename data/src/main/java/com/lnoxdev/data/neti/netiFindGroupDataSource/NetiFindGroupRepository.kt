package com.lnoxdev.data.neti.netiFindGroupDataSource

import android.util.Log
import com.lnoxdev.data.InternetException
import com.lnoxdev.data.ParseException
import com.lnoxdev.data.neti.NetiApi

class NetiFindGroupRepository(private val netiApi: NetiApi) {

    suspend fun findGroup(searchText: String): List<String> {
        val dataString = findGroupRequest(searchText)
        val result = parseGroupList(dataString)
        return result
    }

    private suspend fun findGroupRequest(search: String): String {
        try {
            return netiApi.getGroupSearchResult(search).body()?.string() ?: throw Exception()
        } catch (e: Exception) {
            Log.e("FindGroupDataSource", e.toString())
            throw InternetException("Failed load groups")
        }
    }

    private fun parseGroupList(data: String): List<String> {
        try {
            return HtmlFindGroupParser.parseGroups(data)
        } catch (e: Exception) {
            Log.e("FindGroupDataSource", e.toString())
            throw ParseException("Failed parse groups")
        }
    }
}