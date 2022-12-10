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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentAddAlarmsBinding
import dev.brdlf.medtracker.viewmodel.DEBUG_TAG
import dev.brdlf.medtracker.viewmodel.MedBuilderViewModel
import dev.brdlf.medtracker.viewmodel.MedBuilderViewModelFactory

class AddAlarmsFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val navigationArgs: AddAlarmsFragmentArgs by navArgs()
    private var _binding: FragmentAddAlarmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedBuilderViewModel by activityViewModels {
        MedBuilderViewModelFactory()
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
        binding.title.text = viewModel.medName.value

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.durations,
            android.R.layout.simple_spinner_item
        ).also { spinAdapter ->
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.frequencyUnit.adapter = spinAdapter
            binding.frequencyUnit.onItemSelectedListener = this
        }

        val id = navigationArgs.itemId
        if (id > 0) {
//            lVM.retrieveMed(id).observe(this.viewLifecycleOwner) { selectedItem ->
//                med = selectedItem
//            }
            binding.finishButton.setOnClickListener {
                editMed()
            }
        } else {
            binding.finishButton.setOnClickListener {
                addNewMed()
            }
        }

        viewModel.alarmCount.observe(this.viewLifecycleOwner) {
            binding.alarmsAdapter?.setSize(it)
        }
    }

    private fun addNewMed() {
//        Does nothing, just returns to add, silly code! can remove
        returnToAdd()
    }
    private fun editMed() {
//        ?MedVM updateMed(med)
//        this is when the alarmList needs to be converted to string and sent to the med
        returnToDetails()
    }

    private fun returnToAdd() {
        val action = AddAlarmsFragmentDirections.actionAlarmsAddFragmentToMedsAddFragment(
            getString(R.string.meds_add_fragment_label),
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
        TODO("Not yet implemented")
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