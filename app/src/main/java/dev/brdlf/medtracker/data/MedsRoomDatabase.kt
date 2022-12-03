package dev.brdlf.medtracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.brdlf.medtracker.model.Med

@Database(entities = [Med::class], version = 1, exportSchema = false)
abstract class MedsRoomDatabase : RoomDatabase() {

    abstract fun medDao(): MedDao

    companion object {
        @Volatile
        private var INSTANCE: MedsRoomDatabase? = null

        fun getDatabase(context: Context): MedsRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MedsRoomDatabase::class.java,
                    "meds_database"
                )
                    //TODO: change migration style once you learn how that's supposed to actually work.
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}