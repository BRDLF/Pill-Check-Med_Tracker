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
//        return alarms.replace(";", "\n")
        val testString = "EX;10:00;12:00;2:00"
        val split = testString.split(";")
        when (split[0]) {
            "EX" -> return split.drop(1).joinToString("\n")
        }
        return "Test Alarm\nTest Alarm 2"
    }
}