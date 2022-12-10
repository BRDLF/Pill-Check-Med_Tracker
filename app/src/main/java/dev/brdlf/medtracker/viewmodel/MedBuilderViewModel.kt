package dev.brdlf.medtracker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

const val DEBUG_TAG = "DEBUG TAG"

class MedBuilderViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()

    val alarmCount: MutableLiveData<String> = MutableLiveData<String>("1")

//    val isTethered: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val alarmData: MutableLiveData<List<String>> = MutableLiveData(listOf())


    fun getAlarmList(): String {
        Log.d(DEBUG_TAG, "Sending an alarmList that looks like: ${alarmData.value}")
        return alarmData.value?.toString()?: "alarmList No Value"
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

class MedBuilderViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedBuilderViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedBuilderViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}