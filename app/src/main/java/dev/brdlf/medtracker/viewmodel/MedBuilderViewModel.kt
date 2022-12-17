package dev.brdlf.medtracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.unwrap

const val DEBUG_TAG = "DEBUG TAG"


class MedBuilderViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()
    val medDesc: MutableLiveData<String> = MutableLiveData<String>()

    private val _alarmData: MutableLiveData<List<Pair<Int, Int>>> = MutableLiveData(listOf())
    val alarmData: LiveData<List<Pair<Int,Int>>> get() = _alarmData

    fun clearVM(){
        medName.value = ""
        medDesc.value = ""
        _alarmData.value = mutableListOf()
    }

    //Translate entire data set to/from string
    //Wrap, Needed only by builderVM
    private fun List<Pair<Int, Int>>.wrap(): String {
        return this.joinToString(";") { String.format("%d:%d", it.first, it.second) }
    }
    fun wrap(): String {
//        return getSortedSet().joinToString(";")
        return alarmData.value?.wrap()?: "ERROR"
    }

    fun unwrap(source: String) {
        val sortedSource = source.split(";").filterNot{ it.isEmpty() }
        Log.d(DEBUG_TAG, "In setAlarmFromString, sortedSource is $sortedSource")
        _alarmData.value = source.unwrap()
    }

    //Edit Individual Data
    fun addAlarm(insert: Pair<Int, Int>): Boolean{
        _alarmData.value = alarmData.value?.plus(insert)?.distinct()?: return false
        sortAlarms()
        return true
    }
    fun updateAlarmAt(index: Int, new: Pair<Int, Int>) {
        Log.d(DEBUG_TAG, "Running UpdateAt")
        _alarmData.value = alarmData.value?.mapIndexed{ i, original -> if (i == index) new else original }?.distinct()
        sortAlarms()
    }
    fun removeAlarmAt(index: Int, str: Pair<Int, Int>) {
        if (alarmData.value?.get(index) == str) {
            Log.d(DEBUG_TAG, "Removing $str at $index")
            _alarmData.value = alarmData.value?.filterIndexed{i, _ -> i != index}
        }
        else Log.d(DEBUG_TAG, "Unable to remove alarm, $str was not at $index")
    }
    private val myCompare = compareBy<Pair<Int, Int>>({it.first}, {it.second})
    private fun sortAlarms() {
        _alarmData.value = alarmData.value?.sortedWith(myCompare)
    }
}

class MedBuilderViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedBuilderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedBuilderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}