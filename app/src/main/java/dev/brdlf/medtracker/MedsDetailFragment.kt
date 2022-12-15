package dev.brdlf.medtracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.brdlf.medtracker.databinding.FragmentMedsDetailBinding
import dev.brdlf.medtracker.model.Med
import dev.brdlf.medtracker.viewmodel.MedsViewModel
import dev.brdlf.medtracker.viewmodel.MedsViewModelFactory

class MedsDetailFragment : Fragment() {

    private val navigationArgs: MedsDetailFragmentArgs by navArgs()
    lateinit var med: Med

    private val viewModel: MedsViewModel by activityViewModels {
        MedsViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }
    private var _binding: FragmentMedsDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedsDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.itemId
        viewModel.retrieveMed(id).observe(this.viewLifecycleOwner) {
                selectedItem ->
            med = selectedItem
            bind(med)
        }
    }

    private fun bind(med: Med) {
        binding.thisMed = med
        binding.editButton.setOnClickListener { editMed() }
        binding.deleteButton.setOnClickListener{ deleteMed() }
        binding.alarmButton.setOnClickListener { editAlarms() }
    }

    private fun editMed() {
        val action = MedsDetailFragmentDirections.actionMedsDetailFragmentToMedsAddFragment(
            getString(R.string.edit_fragment_title),
            med.id
        )
        this.findNavController().navigate(action)
    }
    private fun deleteMed(){
        viewModel.deleteMed(med)
        val action = MedsDetailFragmentDirections.actionMedsDetailFragmentToMedsListFragment()
        this.findNavController().navigate(action)
    }

    private fun editAlarms() {
        val action = MedsDetailFragmentDirections.actionMedsDetailFragmentToAlarmsAddFragment(itemId = med.id)
        this.findNavController().navigate(action)
    }
}