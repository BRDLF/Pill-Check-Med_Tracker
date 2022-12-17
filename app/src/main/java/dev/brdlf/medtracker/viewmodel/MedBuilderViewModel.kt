package dev.brdlf.medtracker.viewmodel

import android.icu.text.DateFormat
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.Array.set
import java.util.Calendar

const val DEBUG_TAG = "DEBUG TAG"


class MedBuilderViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()
    val medDesc: MutableLiveData<String> = MutableLiveData<String>()

    private val _alarmData: MutableLiveData<List<Pair<Int, Int>>> = MutableLiveData(listOf<Pair<Int,Int>>())
    val alarmData: LiveData<List<Pair<Int,Int>>> get() = _alarmData

    fun clearVM(){
        medName.value = ""
        medDesc.value = ""
        _alarmData.value = mutableListOf()
    }

    //Translate entire data set to/from string
    fun setAlarmDataFromString(source: String) {
        val sortedSource = source.split(";").filterNot{ it.isEmpty() }
        Log.d(DEBUG_TAG, "In setAlarmFromString, sortedSorce is $sortedSource")
        setAlarmData(sortedSource)
    }
    private fun setAlarmData(toAdd :List<String>) {
        Log.d(DEBUG_TAG, "in setAlarmData, adding $toAdd to ${_alarmData.value}")
        _alarmData.value = toAdd.map{ stringToPair(it) }
        Log.d(DEBUG_TAG, "in setAlarmData, alarmData is now ${_alarmData.value}")
    }
    fun alarmSetToString(): String {
        return getSortedSet().joinToString(";")
    }
    private fun getSortedSet(): List<String> {
        return alarmData.value?.map { pairToString(it) }?: listOf("ERROR")
    }
    //Edit Individual Data
    fun addToAlarms(insert: String): Boolean{
        Log.d(DEBUG_TAG, "addToAlarms: $insert")
        Log.d(DEBUG_TAG, "Before: ${_alarmData.value} & ${alarmData.value}")
        _alarmData.value = alarmData.value?.plus(stringToPair(insert))?.distinct()
        sortAlarms()
        Log.d(DEBUG_TAG, "After: ${_alarmData.value} & ${alarmData.value}")
        return true
    }
    //TODO tidy this up to take TimeDateFormat
    private fun pairToString(a: Pair<Int, Int>): String {
        val c = Calendar.Builder().set(Calendar.HOUR_OF_DAY, a.first).set(Calendar.MINUTE, a.second).build()
        val cBuilder: (Pair<Int, Int>) -> Calendar = { a -> Calendar.Builder().set(Calendar.HOUR_OF_DAY, a.first).set(Calendar.MINUTE, a.second).build()}
        val cToString: (Calendar) -> String = {c -> DateFormat.getDateInstance().format(c)}
        Log.d(DEBUG_TAG, c.toString())
        return String.format("%d:%02d", a.first, a.second)
    }
    private fun stringToPair(s: String): Pair<Int, Int> {
        return Pair(s.split(":").first().toInt(), s.split(":").last().toInt())
    }
    fun updateAt(index: Int, new: String) {
        Log.d(DEBUG_TAG, "Running UpdateAt")
        _alarmData.value = alarmData.value?.mapIndexed{ i, original -> if (i == index) stringToPair(new) else original }?.distinct()
        sortAlarms()
    }
    fun removeAlarmAt(index: Int, str: String) {
        if (alarmData.value?.get(index) == stringToPair(str)) {
            Log.d(DEBUG_TAG, "Removing $str at $index")
            _alarmData.value = alarmData.value?.filterIndexed{i, _ -> i != index}
        }
        else Log.d(DEBUG_TAG, "Unable to remove alarm, $str was not at $index")
    }
    private val myCompare = compareBy<Pair<Int, Int>>({it.first}, {it.second})
    private fun sortAlarms() {
        _alarmData.value = alarmData.value?.sortedWith(myCompare)
    }
    fun removeFromAlarms(str: String) {
        _alarmData.value = alarmData.value?.minus(stringToPair(str))
    }

}

class MedBuilderViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedBuilderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedBuilderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}