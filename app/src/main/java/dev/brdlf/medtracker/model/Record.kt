package dev.brdlf.medtracker.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

@Entity(tableName = "records",
    foreignKeys = [ForeignKey(
        entity = Med::class,
        parentColumns = ["id"],
        childColumns = ["med_id"],
        onDelete = ForeignKey.NO_ACTION)
    ])
data class Record(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "med_id")
    val medID: Int,
    @ColumnInfo(name = "target_time")
    val targetTime: String,
    @ColumnInfo(name = "finish_time")
    val finishTime: String? = null,
    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean = false
)
