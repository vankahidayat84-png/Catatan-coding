package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.Folder
import com.example.data.Materi
import com.example.data.Script
import com.example.data.StudyVaultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudyVaultViewModel(private val repository: StudyVaultRepository) : ViewModel() {

    // Folders
    val allFolders = repository.getAllFolders().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _searchFolderQuery = MutableStateFlow("")
    val searchFolderQuery: StateFlow<String> = _searchFolderQuery
    val searchedFolders = _searchFolderQuery.flatMapLatest { repository.searchFolders(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchFolderQuery(query: String) { _searchFolderQuery.value = query }
    fun insertFolder(folder: Folder) = viewModelScope.launch { repository.insertFolder(folder) }
    fun updateFolder(folder: Folder) = viewModelScope.launch { repository.updateFolder(folder) }
    fun deleteFolder(folder: Folder) = viewModelScope.launch { repository.deleteFolder(folder) }

    // Materis
    val allMateris = repository.getAllMateris().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val favoriteMateris = repository.getFavoriteMateris().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _searchMateriQuery = MutableStateFlow("")
    val searchMateriQuery: StateFlow<String> = _searchMateriQuery
    val searchedMateris = _searchMateriQuery.flatMapLatest { repository.searchMateris(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchMateriQuery(query: String) { _searchMateriQuery.value = query }
    fun insertMateri(materi: Materi) = viewModelScope.launch { repository.insertMateri(materi) }
    fun updateMateri(materi: Materi) = viewModelScope.launch { repository.updateMateri(materi) }
    fun deleteMateri(materi: Materi) = viewModelScope.launch { repository.deleteMateri(materi) }
    fun getMaterisByFolder(folderId: Int) = repository.getMaterisByFolder(folderId)

    // Scripts
    val allScripts = repository.getAllScripts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val favoriteScripts = repository.getFavoriteScripts().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    private val _searchScriptQuery = MutableStateFlow("")
    val searchScriptQuery: StateFlow<String> = _searchScriptQuery
    val searchedScripts = _searchScriptQuery.flatMapLatest { repository.searchScripts(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchScriptQuery(query: String) { _searchScriptQuery.value = query }
    fun insertScript(script: Script) = viewModelScope.launch { repository.insertScript(script) }
    fun updateScript(script: Script) = viewModelScope.launch { repository.updateScript(script) }
    fun deleteScript(script: Script) = viewModelScope.launch { repository.deleteScript(script) }
    fun getScriptsByFolder(folderId: Int) = repository.getScriptsByFolder(folderId)

    // App Data Resets
    fun resetMateris() = viewModelScope.launch { repository.deleteAllMateris() }
    fun resetScripts() = viewModelScope.launch { repository.deleteAllScripts() }
    fun resetAllData() = viewModelScope.launch { repository.resetAllData() }
}

class StudyVaultViewModelFactory(private val repository: StudyVaultRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudyVaultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudyVaultViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
