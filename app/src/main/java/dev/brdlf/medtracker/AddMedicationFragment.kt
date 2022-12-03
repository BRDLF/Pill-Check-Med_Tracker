package dev.brdlf.medtracker

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentMedsAddBinding
import dev.brdlf.medtracker.model.Med
import dev.brdlf.medtracker.viewmodel.MedAddViewModel
import dev.brdlf.medtracker.viewmodel.MedsListViewModel
import dev.brdlf.medtracker.viewmodel.MedsListViewModelFactory

class AddMedicationFragment : Fragment(){

    private val navigationArgs: AddMedicationFragmentArgs by navArgs()

    private var _binding: FragmentMedsAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedAddViewModel by viewModels()

    private val lVM: MedsListViewModel by activityViewModels {
        MedsListViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }
    private lateinit var med: Med

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedsAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO Change Test Text
    //TODO implement Data Binding
    private fun isValid(): Boolean {
        return lVM.isEntryValid(
            binding.inputMedName.text.toString(),
            "Test Description",
            "Test Trackers",
        )
    }

    //TODO Change Test Text
    private fun addNewMed() {
        if (isValid()) {
            lVM.addMed(
                binding.inputMedName.text.toString(),
                "Test Description",
                "Test Trackers",
            )
            returnToMedsList()
        }
    }

    //TODO Change Test Text
    private fun updateMed() {
        if (isValid()) {
            lVM.updateMed(
                this.navigationArgs.itemId,
                this.binding.inputMedName.text.toString(),
                "Test Description",
                "Test Trackers",
            )
            returnToMedsList()
        }
    }

    private fun returnToMedsList() {
        val action = AddMedicationFragmentDirections.actionMedsAddFragmentToMedsListFragment()
        findNavController().navigate(action)
    }

    private fun bind(med: Med){
        binding.apply {
            inputMedName.setText(med.name)
            finishButton.setOnClickListener { updateMed() }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.alarmsAdapter = AlarmsListAdapter()
//        binding.alarmsRecycler.adapter = AlarmsListAdapter()
        binding.vm = viewModel

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.durations,
            android.R.layout.simple_spinner_item
        ).also { spinAdapter ->
            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = spinAdapter
        }

        val id = navigationArgs.itemId
        if (id > 0) {
            lVM.retrieveMed(id).observe(this.viewLifecycleOwner) { selectedItem ->
                med = selectedItem
                bind(med)
            }
        } else {
            binding.finishButton.setOnClickListener {
                addNewMed()
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.alarmCount.observe(this.viewLifecycleOwner) {
            binding.alarmsAdapter?.setSize(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}