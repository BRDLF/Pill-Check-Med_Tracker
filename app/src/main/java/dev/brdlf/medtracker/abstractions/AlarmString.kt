package dev.brdlf.medtracker.abstractions

import android.icu.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.*

class AlarmString {
    companion object {
        //Unwrap
        fun String.unwrap(): List<Pair<Int, Int>> {
            if (this.isBlank()) return emptyList()
            val individuals = this.split(";")
            val asPair = individuals.map {
                val (hour, minute) = it.split(":").map { each -> each.toInt() }
                Pair(hour, minute)
            }
            return asPair
        }

        //Format each in list, then consolidate
        fun List<Pair<Int, Int>>.formatConsolidate(): String {
            return this.joinToString("\n") {it.formatToString()}
        }

        //Format from DateTime
        fun Pair<Int,Int>.formatToString(): String {
            val pairToCalendar: (Pair<Int, Int>) -> Calendar = { a -> Calendar.Builder()
                .set(Calendar.HOUR_OF_DAY, a.first)
                .set(Calendar.MINUTE, a.second)
                .build()
            }
            val calendarToString: (Calendar) -> String = { c ->
                DateFormat.getTimeInstance(SHORT)
                    .format(c.time)
            }
            return calendarToString(pairToCalendar(this))
        }

        //Consolidate
        fun List<String>.consolidate(): String {
            return this.joinToString("\n")
        }
    }
}