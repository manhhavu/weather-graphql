@file:kotlin.jvm.JvmMultifileClass
@file:kotlin.jvm.JvmName("JSONKt")

package com.manhhavu.experiments.graphql

import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

fun JSONObject.asMap(): Map<String, Any?> {
    return asMap(this)
}

// Inspired by https://gist.github.com/codebutler/2339666
private fun asMap(obj: JSONObject): Map<String, Any?> {
    val map = HashMap<String, Any?>()
    val keys = obj.keys()
    while (keys.hasNext()) {
        val key = keys.next() as String
        map.put(key, fromJson(obj.get(key)))
    }
    return map
}

private fun asList(array: JSONArray): List<Any?> {
    val list = ArrayList<Any?>()
    for (i in 0..(array.length() - 1)) {
        list.add(fromJson(array.get(i)))
    }
    return list
}

private fun fromJson(json: Any): Any? {
    if (json === JSONObject.NULL) {
        return null
    } else if (json is JSONObject) {
        return asMap(json)
    } else if (json is JSONArray) {
        return asList(json)
    } else {
        return json
    }
}