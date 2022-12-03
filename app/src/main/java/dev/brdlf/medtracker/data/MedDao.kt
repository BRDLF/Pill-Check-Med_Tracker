package dev.brdlf.medtracker.data

import androidx.room.*
import dev.brdlf.medtracker.model.Med
import kotlinx.coroutines.flow.Flow

@Dao
interface MedDao {

    @Query("SELECT * FROM meds ORDER BY name ASC")
    fun getMeds(): Flow<List<Med>>

    @Query("SELECT * FROM meds WHERE id = :id")
    fun getMed(id: Int): Flow<Med>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(med: Med)

    @Update
    suspend fun update(med: Med)

    @Delete
    suspend fun delete(med: Med)
}