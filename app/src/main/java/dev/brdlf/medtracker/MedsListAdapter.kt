package dev.brdlf.medtracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.brdlf.medtracker.databinding.ViewMedsListBinding
import dev.brdlf.medtracker.model.Med

class MedsListAdapter(private val onMedClicked: (Med) -> Unit) :
    ListAdapter<Med, MedsListAdapter.MedsViewHolder>(DiffCallback){

        class MedsViewHolder(private val binding: ViewMedsListBinding) :
                RecyclerView.ViewHolder(binding.root) {

                    fun bind(med: Med){
                        binding.medName.text = med.name
                        binding.medDesc.text = med.description
                    }
                }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Med>() {
            override fun areItemsTheSame(oldItem: Med, newItem: Med): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Med, newItem: Med): Boolean {
                return (
                        oldItem.name == newItem.name &&
                        oldItem.description == newItem.description &&
                        oldItem.trackers == newItem.trackers
                        )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedsViewHolder {
        return MedsViewHolder(
            ViewMedsListBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: MedsViewHolder, position: Int) {
        //TODO: implement onBindViewHolder
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onMedClicked(current)
        }
        holder.bind(current)
    }
}