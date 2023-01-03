package dev.brdlf.medtracker.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AlertWithMeds(
    @Embedded val alert: Alert,
    @Relation(
        parentColumn = "alarmTime",
        entityColumn = "id",
        associateBy = Junction(MedAlertCrossRef::class)
    )
    val meds: List<Med>
)