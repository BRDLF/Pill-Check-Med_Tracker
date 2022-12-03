package dev.brdlf.medtracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MedAddViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()

    val alarmCount: MutableLiveData<String> = MutableLiveData<String>("1")

    val isTethered: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun updateCount(i: String) {
        alarmCount.value = i.toIntOrNull()?.coerceIn(1..99)?.toString()?: "1"
    }


    fun setTethered(boolean: Boolean) {
        isTethered.value = boolean
    }

}