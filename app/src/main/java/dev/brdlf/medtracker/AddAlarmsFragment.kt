package dev.brdlf.medtracker

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentAddAlarmsBinding
import dev.brdlf.medtracker.model.Med
import dev.brdlf.medtracker.viewmodel.*

const val UPDATE = 2
const val DELETE = 4

class AddAlarmsFragment : Fragment(), OnTimeSetListener {

    private val navigationArgs: AddAlarmsFragmentArgs by navArgs()
    private var _binding: FragmentAddAlarmsBinding? = null
    private val binding get() = _binding!!

    private val builderViewModel: MedBuilderViewModel by activityViewModels{MedBuilderViewModelFactory()}
    private val totalViewModel: MedsViewModel by activityViewModels {
        MedsViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }
    private lateinit var med: Med

    private fun bind(med: Med){
        Log.d(DEBUG_TAG, "Binding")
        binding.apply {
            medName.text = med.name
            vm?.medDesc?.value = med.description
            vm?.unwrap(med.alarms)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = builderViewModel

        //SETTING RECYCLERVIEW (alarmAdapter
        val alarmAdapter = AlarmsListAdapter(timePickerMachine, sendToVM)
        binding.alarmsAdapter = alarmAdapter

        //SPINNER ADAPTER SETUP
//        ArrayAdapter.createFromResource( requireContext(), R.array.durations, android.R.layout.simple_spinner_item)
//            .also { spinAdapter ->
//            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            binding.frequencyUnit.adapter = spinAdapter
//            binding.frequencyUnit.onItemSelectedListener = this
//        }

        //HANDLING SOURCE ACTION
        val itemId = navigationArgs.itemId
        Log.d(DEBUG_TAG, "Entered AddAlarms with ID: $itemId")
        if (navigationArgs.external) {
            builderViewModel.clearVM()
            totalViewModel.retrieveMed(itemId).observe(this.viewLifecycleOwner) {
                med = it
                bind(it)
            }
            binding.finishButton.setOnClickListener { returnToDetails() }
        } else {
            binding.finishButton.setOnClickListener { returnToAdd() }
        }

        //Observe changes to alarmData
        builderViewModel.alarmData.observe(this.viewLifecycleOwner) {
            alarmAdapter.submitList(it)
        }

        binding.buttonAddAlarm.setOnClickListener{
            TimePickerFragment(this).show(parentFragmentManager, "New Alarm")
        }
    }

    private fun isValid(): Boolean {
        val mName = builderViewModel.medName.value?: "Can't find mName"
        val mDesc = builderViewModel.medDesc.value?: "Can't find mDesc"
        val sTS = builderViewModel.wrap()

        Log.d(DEBUG_TAG, "isValid; Name: $mName, Desc: $mDesc, Alarms: $sTS")
        return totalViewModel.isEntryValid(
            builderViewModel.medName.value?: "ERROR. CHECK IS VALID",
            builderViewModel.medDesc.value?: "ERROR. CHECK IS VALID"
        )
    }

    //NAV
    private fun returnToAdd() {
        val title = if (navigationArgs.itemId > 0) getString(R.string.edit_fragment_title) else getString(R.string.meds_add_fragment_label)
        val action = AddAlarmsFragmentDirections.actionAlarmsAddFragmentToMedsAddFragment(
            title = title,
            itemId = navigationArgs.itemId,
            external = false)
        findNavController().navigate(action)
    }
    private fun returnToDetails() {
        if (isValid()){
            totalViewModel.updateMed(
                this.navigationArgs.itemId,
                builderViewModel.medName.value?: "ERROR. Check ReturnToDetails",
                builderViewModel.medDesc.value?: "ERROR. Check ReturnToDetails",
                builderViewModel.wrap()
            )
            val action = AddAlarmsFragmentDirections.actionAlarmsAddFragmentToMedsDetailFragment(navigationArgs.itemId)
            findNavController().navigate(action)
        }
    }

    private val timePickerMachine: (Int, Int, Int, OnTimeSetListener) -> Unit = { position, hour, minute, onTimeSetListener ->
        TimePickerFragment(onTimeSetListener, hour, minute).show(parentFragmentManager, position.toString())
    }

    private val sendToVM: (Int, Pair<Int, Int>, Int) -> Unit = {
        position, value, operation ->
        Log.d(DEBUG_TAG, "Sending to VM pos $position value $value")
        when(operation) {
            UPDATE -> builderViewModel.updateAlarmAt(position, value)
            DELETE -> builderViewModel.removeAlarmAt(position, value)
        }
    }
    private val addAlarm: (Pair<Int, Int>) -> Unit = { str ->
        Log.d(DEBUG_TAG, "Adding an alarm")
        builderViewModel.addAlarm(str)
    }
    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) = addAlarm(Pair(p1, p2))

    //Spinner Fun
    //    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        builderViewModel.alarmCount.value = p0?.getItemAtPosition(p2).toString()
//    }
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//        Log.d(DEBUG_TAG, "onNothingSelected in addAlarmsFragment")
//    }
}

class TimePickerFragment(private val lis: OnTimeSetListener, private val hour: Int = 0, private val minute: Int = 0): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, lis, hour, minute, android.text.format.DateFormat.is24HourFormat(activity))
    }
}