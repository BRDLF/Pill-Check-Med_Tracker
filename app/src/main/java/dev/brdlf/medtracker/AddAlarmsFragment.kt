package dev.brdlf.medtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentAddAlarmsBinding
import dev.brdlf.medtracker.databinding.FragmentMedsAddBinding
import dev.brdlf.medtracker.viewmodel.MedAddViewModel
import dev.brdlf.medtracker.viewmodel.MedsListViewModel
import dev.brdlf.medtracker.viewmodel.MedsListViewModelFactory

class AddAlarmsFragment : Fragment() {

    private val navigationArgs: AddMedicationFragmentArgs by navArgs()
    private var _binding: FragmentAddAlarmsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MedsListViewModel by activityViewModels {
        MedsListViewModelFactory(
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

    }
}