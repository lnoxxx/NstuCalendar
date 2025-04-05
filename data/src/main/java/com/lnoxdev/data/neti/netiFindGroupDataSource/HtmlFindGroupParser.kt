package com.lnoxdev.data.neti.netiFindGroupDataSource

import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object HtmlFindGroupParser {

    fun parseGroups(dataString: String): List<String> {
        val jsonObject = JSONObject(dataString)
        val items = jsonObject.getString("items")
        val doc: Document =
            Jsoup.parse(items.replace("\n", "").replace("\t", ""))
        val groupItems = doc.body().select("a")
        val groupList = groupItems.map { it.text().toString() }
        return groupList
    }
}