package dev.brdlf.medtracker

import android.app.TimePickerDialog.OnTimeSetListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.databinding.FragmentMedsAddBinding
import dev.brdlf.medtracker.databinding.ViewAlarmBinding
import dev.brdlf.medtracker.viewmodel.MedAddViewModel

class AlarmsListAdapter : RecyclerView.Adapter<AlarmsListAdapter.AlarmViewHolder>() {
    private var size: Int = 1

    class AlarmViewHolder(private val binding: ViewAlarmBinding) : RecyclerView.ViewHolder(binding.root), OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            binding.timeButton.text = "HH:mm".format(hourOfDay, minute)
        }

        fun bind(clickListener: (Int) -> Unit) {
            binding.timeButton.setOnClickListener{
                clickListener(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        return AlarmViewHolder(
            ViewAlarmBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    //TODO set an OnClick to create a TimePicker and send to the view
    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
    }

    //TODO change getItemCount to follow rules of viewModel
    override fun getItemCount(): Int = size

    fun setSize(s: String) {
        size = s.toIntOrNull() ?: 1
    }

    //TODO implement function to return the string list to be converted into an alarmbuilder, thingy.
    fun returnTimes(): List<String> {
        return listOf<String>()
    }
}