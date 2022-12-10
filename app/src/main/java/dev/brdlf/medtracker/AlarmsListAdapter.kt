package dev.brdlf.medtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TimePicker
import android.app.TimePickerDialog
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.databinding.ViewAlarmAddBinding
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG

//TODO convert this to a DiffUtil and track from a viewModel
class AlarmsListAdapter(private val sentListener: (Int, TimePickerDialog.OnTimeSetListener) -> Unit, private val sendUpToViewModel: (Int, String) -> Unit) :
    RecyclerView.Adapter<AlarmsListAdapter.AlarmViewHolder>() {
    private var size: Int = 1

    class AlarmViewHolder(private val binding: ViewAlarmAddBinding, private val sendUpToViewModel: (Int, String) -> Unit) : RecyclerView.ViewHolder(binding.root), TimePickerDialog.OnTimeSetListener {

        override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
            String.format("%2d:%02d", p1, p2).also {
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
            ViewAlarmAddBinding.inflate(
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
}