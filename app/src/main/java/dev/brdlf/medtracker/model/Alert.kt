package dev.brdlf.medtracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alerts")
data class Alert(
    @PrimaryKey(autoGenerate = false)
    val alarmTime: Int,
)