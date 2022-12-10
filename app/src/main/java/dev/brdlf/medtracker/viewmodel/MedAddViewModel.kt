package dev.brdlf.medtracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val DEBUG_TAG = "DEBUG TAG"

class MedAddViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()

    val alarmCount: MutableLiveData<String> = MutableLiveData<String>("1")

    val isTethered: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val alarmData: MutableLiveData<List<String>> = MutableLiveData(listOf())
    fun getAlarmList(): String {
        Log.d(DEBUG_TAG, "Sending a fooList that looks like: ${alarmData.value}")
        return alarmData.value?.toString()?: "fooList No Value"
    }

    fun updateAt(index: Int, s: String) {
        Log.d(DEBUG_TAG, "Running UpdateAt")
        val newSize = maxOf(alarmData.value?.size?: -1, index + 1)
        val mL = MutableList<String>(newSize){""}
        alarmData.value?.forEachIndexed{ oldIndex, oldString -> mL[oldIndex] = oldString }
        mL[index] = s
        alarmData.value = mL
    }
}