package dev.brdlf.medtracker.model

import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "meds")
data class Med(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val trackers: String = ""
    )