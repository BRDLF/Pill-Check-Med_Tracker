package dev.brdlf.medtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.databinding.ViewAlarmAddBinding
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG

class AlarmsListAdapter(private val timePickingMachine: (Int, Int, Int, TimePickerDialog.OnTimeSetListener) -> Unit,
                        private val bigListener: (Int, String, Int) -> Unit) :
    ListAdapter<String, AlarmsListAdapter.AlarmViewHolder>(DiffCallback) {

    class AlarmViewHolder(private val binding: ViewAlarmAddBinding, private val bigListener: (Int, String, Int) -> Unit) : RecyclerView.ViewHolder(binding.root), TimePickerDialog.OnTimeSetListener {

        override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
            String.format("%d:%02d", p1, p2).also {
                Log.d(DEBUG_TAG, "Edit alarm *$it*")
                binding.timeButton.text = it
                bigListener(adapterPosition, it, UPDATE)
            }
        }

        fun bind(alarmString: String, clickListener: (Int, Int, Int, TimePickerDialog.OnTimeSetListener) -> Unit, ) {
            binding.timeButton.text = alarmString
            binding.editButton.setOnClickListener{
                val (hour, minute) = binding.timeButton.text.split(":").map{it.toInt()}
                clickListener(adapterPosition, hour, minute, this)
            }
            binding.deleteButton.setOnClickListener {
                bigListener(adapterPosition, binding.timeButton.text.toString(), DELETE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            ViewAlarmAddBinding.inflate(LayoutInflater.from(parent.context)),
            bigListener)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, timePickingMachine)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}