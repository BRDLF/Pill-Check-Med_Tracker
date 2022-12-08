package dev.brdlf.medtracker.viewmodel

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MedAddViewModel : ViewModel() {
    val medName: MutableLiveData<String> = MutableLiveData<String>()

    val alarmCount: MutableLiveData<String> = MutableLiveData<String>("1")

    val isTethered: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    val alarmList: MutableLiveData<String> = MutableLiveData<String>("")

    fun updateCount(i: CharSequence) {
        alarmCount.value = i.toString().toIntOrNull()?.coerceIn(1..99)?.toString()?: "1"
    }

    fun setTethered(boolean: Boolean) {
        isTethered.value = boolean
    }
    val updateAlarms: (String) -> Unit = { alarmList.value = it}
}