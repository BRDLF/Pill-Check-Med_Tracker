package dev.brdlf.medtracker.data

import androidx.room.*
import dev.brdlf.medtracker.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMed(med: Med)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlert(alert: Alert)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRelation(medAlertCrossRef: MedAlertCrossRef)

    @Update
    suspend fun update(med: Med)

    @Delete
    suspend fun delete(med: Med)

    @Delete
    suspend fun deleteRelation(medAlertCrossRef: MedAlertCrossRef)

    @Query("SELECT * FROM meds ORDER BY name ASC")
    fun getMeds(): Flow<List<Med>>

    @Query("SELECT * FROM meds WHERE id = :id")
    fun getMed(id: Int): Flow<Med>

    @Query("SELECT * from alerts ORDER BY alarmTime ASC")
    fun getAlerts(): List<Alert>

    @Query("SELECT * FROM alerts WHERE alarmTime = :alarmTime")
    fun getAlerts(alarmTime: Int): Alert

    @Transaction
    @Query("SELECT * FROM meds WHERE id = :medId")
    suspend fun getAlarmsOfMed(medId: Int): List<MedWithAlerts>

    @Transaction
    @Query("SELECT * FROM alerts WHERE alarmTime = :alertTime")
    suspend fun getMedsOfAlarm(alertTime: Int): List<AlertWithMeds>
}