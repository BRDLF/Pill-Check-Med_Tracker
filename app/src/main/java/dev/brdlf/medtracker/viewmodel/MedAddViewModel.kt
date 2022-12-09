package dev.brdlf.medtracker.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

const val DEBUG_TAG = "DEBUG TAG"

class MedAddViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()

    val alarmCount: MutableLiveData<String> = MutableLiveData<String>("1")

    val isTethered: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val fooList: MutableLiveData<List<String>> = MutableLiveData(listOf())
    fun GFL(): String {
        Log.d(DEBUG_TAG, "Sending a fooList that looks like: ${fooList.value}")
        return fooList.value?.toString()?: "fooList No Value"
    }

    fun updateAt(index: Int, s: String) {
        Log.d(DEBUG_TAG, "Running UpdateAt")
        val newSize = maxOf(fooList.value?.size?: -1, index + 1)
        val mL = MutableList<String>(newSize){""}
        fooList.value?.forEachIndexed{ oldIndex, oldString -> mL[oldIndex] = oldString }
        mL[index] = s
        fooList.value = mL
    }
    fun returnAlarms(): String {
        val truncList = fooList.value?.take(alarmCount.value?.toIntOrNull()?:1)?: return "No List?"
        return truncList.joinToString(";")
    }

    private var _alarmList: MutableLiveData<String> = MutableLiveData<String>("")
    val alarmList: LiveData<String> get() = _alarmList

    fun updateCount(i: CharSequence) {
        alarmCount.value = i.toString().toIntOrNull()?.coerceIn(1..99)?.toString()?: "1"
    }

    val updateAlarms: (List<String>) -> Unit = {
        val sb = StringBuilder("ALARMS;")
        for (string in it) {
            sb.append(string)
            sb.append(";")
        }
        _alarmList.value = sb.toString()
    }
}