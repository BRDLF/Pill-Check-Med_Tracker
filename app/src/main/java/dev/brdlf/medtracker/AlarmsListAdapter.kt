package dev.brdlf.medtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.util.Log
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.databinding.ViewAlarmBinding
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG

class AlarmsListAdapter(private val sentListener: (Int, TimePickerDialog.OnTimeSetListener) -> Unit, private val sendUpToViewModel: (Int, String) -> Unit) : RecyclerView.Adapter<AlarmsListAdapter.AlarmViewHolder>() {
    private var size: Int = 1

    //TODO Move to a ViewModel
    private val alarms = mutableMapOf<Int, String>()
    private val updateAlarms: (Int, String) -> Unit = {p, t -> alarms[p] = t}
    private fun buildList(): List<String> {
        val mL = mutableListOf<String>()
        for (index in 0 until size) {
            mL.add("")
        }
        return mL
    }

    class AlarmViewHolder(private val binding: ViewAlarmBinding, private val sendUpToViewModel: (Int, String) -> Unit) : RecyclerView.ViewHolder(binding.root), TimePickerDialog.OnTimeSetListener {

        override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
            "$p1:$p2".also {
                Log.d(DEBUG_TAG, "onTimeSet $it")
                binding.timeButton.text = it
                sendUpToViewModel(adapterPosition, it)
            }
        }

        fun bind(clickListener: (Int, TimePickerDialog.OnTimeSetListener) -> Unit, ) {
            binding.timeButton.setOnClickListener{
                clickListener(adapterPosition, this)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            ViewAlarmBinding.inflate(
                LayoutInflater.from(parent.context)
            ), sendUpToViewModel
        )
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(sentListener)
    }

    //TODO change getItemCount to follow rules of viewModel
    override fun getItemCount(): Int = size

    fun setSize(s: String) {
        size = s.toIntOrNull() ?: 1
    }

    fun returnTimes(): String {
        val sb = StringBuilder()
        for (i in 0..size) {
            sb.append(alarms[i] ?: continue)
            sb.append(";")
        }
        return sb.toString()
    }
}