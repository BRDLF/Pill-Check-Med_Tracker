package dev.brdlf.medtracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val DEBUG_TAG = "DEBUG TAG"


class MedBuilderViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()
    val medDesc: MutableLiveData<String> = MutableLiveData<String>()

    private val _alarmData: MutableLiveData<List<String>> = MutableLiveData(listOf<String>().sortedBy{ it.split(":").first().toIntOrNull() })
    val alarmData: LiveData<List<String>> get() = _alarmData

    fun getAlarmList(): String {
        Log.d(DEBUG_TAG, "Sending an alarmList that looks like: ${alarmData.value}")
        return alarmData.value?.joinToString(";")?: "alarmList No Value"
    }

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
        _alarmData.value = toAdd
        Log.d(DEBUG_TAG, "in setAlarmData, alarmData is now ${_alarmData.value}")
    }
    fun alarmSetToString(): String {
        return getSortedSet().joinToString(";")
    }
    private fun getSortedSet(): List<String> {
        return alarmData.value?: listOf("ERROR")
    }
    //Edit Individual Data
    fun addToAlarms(insert: String): Boolean{
        Log.d(DEBUG_TAG, "addToAlarms: $insert")
        Log.d(DEBUG_TAG, "Before: ${_alarmData.value} & ${alarmData.value}")
        _alarmData.value = alarmData.value?.plus(insert)?.distinct()
        sortAlarms()
        Log.d(DEBUG_TAG, "After: ${_alarmData.value} & ${alarmData.value}")
        return true
    }
    fun updateAt(index: Int, new: String) {
        Log.d(DEBUG_TAG, "Running UpdateAt")
        _alarmData.value = alarmData.value?.mapIndexed{ i, original -> if (i == index) new else original }?.distinct()
        sortAlarms()
    }
    private val myCompare = compareBy<String>({it.split(":").first().toInt()}, {it.split(":").last().toInt()})
    private fun sortAlarms() {
        _alarmData.value = alarmData.value?.sortedWith(myCompare)
    }
    fun removeFromAlarms(str: String) {
        _alarmData.value = alarmData.value?.minus(str)
    }
    fun removeAlarmAt(index: Int, str: String) {
        if (alarmData.value?.get(index) == str) {
            Log.d(DEBUG_TAG, "Removing $str at $index")
            _alarmData.value = alarmData.value?.filterIndexed{i, _ -> i != index}
        }
        else Log.d(DEBUG_TAG, "Unable to remove alarm, $str was not at $index")
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