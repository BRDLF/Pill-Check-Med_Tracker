package dev.brdlf.medtracker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import dev.brdlf.medtracker.viewmodel.MedsListViewModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.brdlf.medtracker.databinding.FragmentMedsListBinding
import dev.brdlf.medtracker.viewmodel.MedsListViewModelFactory

class MedsListFragment : Fragment() {
    private val viewModel: MedsListViewModel by activityViewModels {
        MedsListViewModelFactory(
            (activity?.application as TrackerApplication).database.medDao()
        )
    }

    private var _binding: FragmentMedsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMedsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MedsListAdapter {
            val action =
                MedsListFragmentDirections.Companion.actionMedsListFragmentToMedsDetailFragment(it.id)
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.userMeds.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

        binding.floatingActionButton.setOnClickListener {
            val action = MedsListFragmentDirections.actionMedsListFragmentToMedsAddFragment(
                getString(R.string.meds_add_fragment_label)
            )
            this.findNavController().navigate(action)
        }
    }

}