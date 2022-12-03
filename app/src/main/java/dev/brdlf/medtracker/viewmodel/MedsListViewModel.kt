package dev.brdlf.medtracker.viewmodel

import androidx.lifecycle.*
import dev.brdlf.medtracker.data.MedDao
import dev.brdlf.medtracker.model.Med
import kotlinx.coroutines.launch

class MedsListViewModel(private val medDao: MedDao) : ViewModel() {
    // TODO: ViewModel cleanup. Adjust functions to account for new tracker mode
    val userMeds: LiveData<List<Med>> = medDao.getMeds().asLiveData()

    fun updateMed(itemId: Int, itemName: String, itemDesc: String, itemTrackers: String){
        val updatedMed = getUpdatedMedEntry(itemId, itemName, itemDesc, itemTrackers)
        updateMed(updatedMed)
    }
    private fun updateMed(med: Med) {
        viewModelScope.launch {
            medDao.update(med)
        }
    }

    fun addMed(medName: String, medDesc: String, medTrackers: String) {
        val newMed = getNewMedEntry(medName, medDesc, medTrackers)
        insertMed(newMed)
    }
    private fun insertMed(med: Med) {
        viewModelScope.launch {
            medDao.insert(med)
        }
    }

    fun deleteMed(med: Med) {
        viewModelScope.launch {
            medDao.delete(med)
        }
    }

    fun isEntryValid(medName: String, medDesc: String, medTrackers: String): Boolean {
        if (medName.isBlank() || medDesc.isBlank() || medTrackers.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveMed(id: Int): LiveData<Med> {
        return medDao.getMed(id).asLiveData()
    }

    private fun getNewMedEntry(medName: String, medDesc: String, medTrackers: String): Med {
        return Med(name = medName, description = medDesc, trackers = medTrackers)
    }
    private fun getUpdatedMedEntry(medId: Int, medName: String, medDesc: String, medTrackers: String): Med {
        return Med(
            id = medId,
            name = medName,
            description = medDesc,
            trackers = medTrackers
        )
    }
}

class MedsListViewModelFactory(private val medDao: MedDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedsListViewModel(medDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

