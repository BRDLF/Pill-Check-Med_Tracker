package dev.brdlf.medtracker

import android.app.Application
import dev.brdlf.medtracker.data.MedsRoomDatabase

class TrackerApplication: Application() {
    val database: MedsRoomDatabase by lazy { MedsRoomDatabase.getDatabase(this) }
}