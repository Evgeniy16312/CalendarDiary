package com.example.appcv.model

import com.example.appcv.utils.Formats
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Event(
    @PrimaryKey
    var id: Int? = null,
    var dayId: Long? = null,
    var title: String? = null,
    var timeFrom: Long? = null,
    var timeTo: Long? = null,
    var description: String? = null
) : RealmObject() {
    fun getTimeRange(): String {
        return Formats().timeFormat.format(timeFrom!!).toString() + " - " + Formats()
            .timeFormat.format(timeTo!!).toString()
    }
}