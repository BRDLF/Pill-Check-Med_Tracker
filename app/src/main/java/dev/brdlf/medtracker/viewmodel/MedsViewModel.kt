package dev.brdlf.medtracker.viewmodel

import androidx.lifecycle.*
import dev.brdlf.medtracker.data.MedDao
import dev.brdlf.medtracker.model.Med
import kotlinx.coroutines.launch

class MedsViewModel(private val medDao: MedDao, ) : ViewModel() {

    val userMeds: LiveData<List<Med>> = medDao.getMeds().asLiveData()

    fun stringToSet(s: String): Set<String> = s.split(";").toSet()

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

    fun isEntryValid(medName: String, medDesc: String): Boolean {
        if (medName.isBlank() || medDesc.isBlank()) {
            return false
        }
        return true
    }

    fun retrieveMed(id: Int): LiveData<Med> {
        return medDao.getMed(id).asLiveData()
    }

    private fun getNewMedEntry(medName: String, medDesc: String, medTrackers: String): Med {
        return Med(name = medName, description = medDesc, alarms = medTrackers)
    }
    private fun getUpdatedMedEntry(medId: Int, medName: String, medDesc: String, medTrackers: String): Med {
        return Med(
            id = medId,
            name = medName,
            description = medDesc,
            alarms = medTrackers
        )
    }
}

class MedsViewModelFactory(private val medDao: MedDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedsViewModel(medDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

