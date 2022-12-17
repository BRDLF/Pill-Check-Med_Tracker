package dev.brdlf.medtracker.model

import android.text.TextUtils.split
import androidx.room.*

@Entity(tableName = "meds")
data class Med(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val alarms: String = ""
    ) {
    fun alarmsFormat(): String {
        return alarms.replace(";","\n")
    }
    fun listAlarms(): List<String> {
        return alarms.split(";")
    }
    fun specAlarm(index: Int): String {
        val list = listAlarms()
        return if (index in list.indices) list[index]
            else "ERROR"
    }
    fun alarmToTime(alarm: String): Pair<Int, Int> {
        val (first, second) = alarm.split(":").map{ it.toInt() }
        return Pair(first, second)
    }
}