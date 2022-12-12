package dev.brdlf.medtracker

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentAddAlarmsBinding
import dev.brdlf.medtracker.viewmodel.*

class AddAlarmsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val navigationArgs: AddAlarmsFragmentArgs by navArgs()
    private var _binding: FragmentAddAlarmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedBuilderViewModel by activityViewModels {
        MedBuilderViewModelFactory()
    }
    private val lVM: MedsViewModel by activityViewModels {
        MedsViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        binding.alarmsAdapter = AlarmsListAdapter(alarmListener, updateVMAlarmList)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.durations,
            android.R.layout.simple_spinner_item
        ).also { spinAdapter ->
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.frequencyUnit.adapter = spinAdapter
            binding.frequencyUnit.onItemSelectedListener = this
        }

        val medName: String? = navigationArgs.itemName
        val itemId = navigationArgs.itemId
        Log.d(DEBUG_TAG, "$itemId $medName")
        if (medName != null) {
            binding.medName.text = if (medName.isEmpty()) "A Med with no name" else navigationArgs.itemName
            val newTitle = if (itemId > 0) getString(R.string.edit_fragment_title) else getString(R.string.meds_add_fragment_label)
            binding.finishButton.setOnClickListener { returnToAdd(newTitle) }
        } else {
            lVM.retrieveMed(itemId).observe(this.viewLifecycleOwner) {
                binding.medName.text = it.name
            }
            binding.finishButton.setOnClickListener { returnToDetails() }
        }
        viewModel.alarmCount.observe(this.viewLifecycleOwner) {
            binding.alarmsAdapter?.setSize(it)
        }
    }

    private fun returnToAdd(newTitle: String) {
        val action = AddAlarmsFragmentDirections.actionAlarmsAddFragmentToMedsAddFragment(
            newTitle,
            navigationArgs.itemId)
        findNavController().navigate(action)
    }

    private fun returnToDetails() {
        val action = AddAlarmsFragmentDirections.actionAlarmsAddFragmentToMedsDetailFragment(navigationArgs.itemId)
        findNavController().navigate(action)
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        viewModel.alarmCount.value = p0?.getItemAtPosition(p2).toString()
    }
    override fun onNothingSelected(p0: AdapterView<*>?) {
        Log.d(DEBUG_TAG, "onNothingSelected in addAlarmsFragment")
    }

    private val alarmListener: (Int, TimePickerDialog.OnTimeSetListener) -> Unit = { position, onTimeSetListener ->
        TPF(onTimeSetListener).show(parentFragmentManager, position.toString())
    }
    private val updateVMAlarmList: (Int, String) -> Unit = {
            i, str ->
        Log.d(DEBUG_TAG, "Sending Up!")
        viewModel.updateAt(i, str)
    }
}