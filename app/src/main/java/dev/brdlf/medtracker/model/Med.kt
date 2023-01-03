package dev.brdlf.medtracker.model

import androidx.room.*
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.formatToString
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.unwrap

@Entity(tableName = "meds")
data class Med(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val alarms: String = ""
    ) {
    fun alarmAsFormattedByLocale(): String = alarms.unwrap().joinToString("\n") { it.formatToString() }
}