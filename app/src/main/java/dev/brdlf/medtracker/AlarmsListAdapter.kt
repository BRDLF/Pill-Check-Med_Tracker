package dev.brdlf.medtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.formatToString
import dev.brdlf.medtracker.databinding.ViewAlarmAddBinding
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG

class AlarmsListAdapter(private val timePickingMachine: (Int, Int, Int, TimePickerDialog.OnTimeSetListener) -> Unit,
                        private val bigListener: (Int, Pair<Int, Int>, Int) -> Unit) :
    ListAdapter<Pair<Int,Int>, AlarmsListAdapter.AlarmViewHolder>(DiffCallback) {

    class AlarmViewHolder(private val binding: ViewAlarmAddBinding, private val bigListener: (Int, Pair<Int, Int>, Int) -> Unit) : RecyclerView.ViewHolder(binding.root), TimePickerDialog.OnTimeSetListener {

        override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
            Pair(p1, p2).also {
                Log.d(DEBUG_TAG, "Edit alarm *$it*")
                bigListener(adapterPosition, it, UPDATE)
            }
        }

        fun bind(alarmPair: Pair<Int, Int>, clickListener: (Int, Int, Int, TimePickerDialog.OnTimeSetListener) -> Unit, ) {
            binding.apply {
                timeButton.text = alarmPair.formatToString()
                editButton.setOnClickListener{
                    val (hour, minute) = alarmPair
                    clickListener(adapterPosition, hour, minute, this@AlarmViewHolder)
                }
                deleteButton.setOnClickListener {
                    bigListener(adapterPosition, alarmPair, DELETE)
                }
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
        private val DiffCallback = object : DiffUtil.ItemCallback<Pair<Int, Int>>() {
            override fun areItemsTheSame(oldItem: Pair<Int, Int>, newItem: Pair<Int, Int>): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Pair<Int, Int>, newItem: Pair<Int, Int>): Boolean {
                return oldItem.first == newItem.first && oldItem.second == newItem.second
            }
        }
    }
}