package dev.brdlf.medtracker.model

import androidx.room.Entity

@Entity(primaryKeys = ["alarmTime", "id"])
data class MedAlertCrossRef(
    val alarmTime: Int,
    val id: Int
)