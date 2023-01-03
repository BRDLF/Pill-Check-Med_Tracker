package dev.brdlf.medtracker.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MedWithAlerts(
    @Embedded val med: Med,
    @Relation(
        parentColumn = "id",
        entityColumn = "alarmTime",
        associateBy = Junction(MedAlertCrossRef::class)
    )
    val alerts: List<Alert>
)