package dev.brdlf.medtracker

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.formatConsolidate
import dev.brdlf.medtracker.abstractions.AlarmString.Companion.formatToString
import dev.brdlf.medtracker.databinding.FragmentAddMedsBinding
import dev.brdlf.medtracker.model.Med
import dev.brdlf.medtracker.viewmodel.*

class AddMedicationFragment : Fragment() {

    private val navigationArgs: AddMedicationFragmentArgs by navArgs()

    private var _binding: FragmentAddMedsBinding? = null
    private val binding get() = _binding!!

    private val builderViewModel: MedBuilderViewModel by activityViewModels {
        MedBuilderViewModelFactory()
    }

    private val totalViewModel: MedsViewModel by activityViewModels {
        MedsViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }
    private lateinit var med: Med


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAddMedsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = builderViewModel

        //IF EXTERNAL
        //  ClearVM
        //  IF ID > 0 (External & ID>0)
        //      LOAD MED,
        //      set name, desc from med
        //      set alarms from med

        //(If ID > 0)
        //  save changes
        //Else
        //  create med

        val id = navigationArgs.itemId
        if (navigationArgs.external) {
            builderViewModel.clearVM()
            if (id > 0) {
                Log.d(DEBUG_TAG, "Existing ID: $id")
                totalViewModel.retrieveMed(id).observe(this.viewLifecycleOwner) { selectedItem ->
                    med = selectedItem
                    bind(med)
                }
            }
        }

        if (id > 0) {
            binding.finishButton.text = getString(R.string.save_changes)
            binding.finishButton.setOnClickListener { updateMed() }
        } else {
            binding.finishButton.text = getString(R.string.medTextAdd)
            binding.finishButton.setOnClickListener {
                addNewMed()
            }
        }

        // Setting Alarms Text
        builderViewModel.alarmData.observe(this.viewLifecycleOwner) {
            val alarmsText = if (it.isEmpty()) "No alarms to show" else it.formatConsolidate()
            Log.d(DEBUG_TAG, "Observing med. alarms text being set to $alarmsText")
            binding.alarmDataText.text = alarmsText
        }

        binding.alarmButton.setOnClickListener {
            toAlarms()
        }
    }

    private fun bind(med: Med){
        Log.d(DEBUG_TAG, "In Bind,")
        binding.inputMedName.setText(med.name)
        binding.inputMedDesc.setText(med.description)
        builderViewModel.unwrap(med.alarms)
    }

    private fun isValid(): Boolean {
        return totalViewModel.isEntryValid(
            binding.inputMedName.text.toString(),
            binding.inputMedDesc.text.toString(),
        )
    }
    private fun addNewMed() {
        if (isValid()) {
            totalViewModel.addMed(
                this.binding.inputMedName.text.toString(),
                this.binding.inputMedDesc.text.toString(),
                builderViewModel.wrap(),
            )
            toMedsList()
        }
    }
    private fun updateMed() {
        if (isValid()) {
            totalViewModel.updateMed(
                this.navigationArgs.itemId,
                this.binding.inputMedName.text.toString(),
                this.binding.inputMedDesc.text.toString(),
                builderViewModel.wrap(),
            )
            toDetailsList()
        }
    }

    private fun toAlarms() {
        val action = AddMedicationFragmentDirections.actionMedsAddFragmentToAlarmsAddFragment(itemId = navigationArgs.itemId, external = false)
        this.findNavController().navigate((action))
    }
    private fun toMedsList() {
        val action = AddMedicationFragmentDirections.actionMedsAddFragmentToMedsListFragment()
        findNavController().navigate(action)
    }
    private fun toDetailsList() {
        val action = AddMedicationFragmentDirections.actionMedsAddFragmentToMedsDetailFragment(
            itemId = navigationArgs.itemId
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}